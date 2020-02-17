package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create: 2019-07-18 23:03
 **/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 手机验证码保存到redis中的前缀
     */
    private static final String key_prefix = "USER:VERIFY:";

    /**
     * 验证参数是否可用
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type == 1) {
            //为1时验证用户名
            record.setUsername(data);
        } else if (type == 2) {
            //为2时验证手机
            record.setPhone(data);
        } else {
            return null;
        }

        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    public void sendVerifyCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return;
        }

        //生成验证码
        String code = NumberUtils.generateCode(6);

        //发送消息到rabbitMQ
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        try {
            rabbitTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "verifycode_sms", msg);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

        //把验证码保存到redis,过期时间为5分钟
        redisTemplate.opsForValue().set(key_prefix + phone, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 用户注册；
     * 在使用springboot的stringredistemplate来操作redis时，同样也需要作try-catch处理。
     * 不能影响主业务的进行
     *
     * @param user
     * @param code
     * @return
     */
    public void register(User user, String code) {
        //校验验证码
        //从redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get(key_prefix + user.getPhone());
        if (!org.apache.commons.lang3.StringUtils.equals(code, redisCode)) {
            return;
        }

        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        userMapper.insertSelective(user);

        //删除redis中的验证码
        redisTemplate.delete(key_prefix + user.getPhone());

    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public User query(String username, String password) {
        //根据用户名查询到用户
        User user = new User();
        user.setUsername(username);
        user = this.userMapper.selectOne(user);

        //是否存在判断
        if (user == null) {
            return null;
        }
        //使用用户内的盐加请求的密码进行加密处理，再与数据库内的密码进行判断
        String md5Hex = CodecUtils.md5Hex(password, user.getSalt());
        if (!org.apache.commons.lang3.StringUtils.equals(user.getPassword(), md5Hex)) {
            return null;
        }

        return user;
    }
}
