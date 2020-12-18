package com.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.leyou.common.base.BaseController;
import com.leyou.common.utils.JwtProperties;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RedisUtils;
import com.modules.api.comon.weixin.entity.PhoneQuery;
import com.modules.api.comon.weixin.entity.TokenQuery;
import com.modules.api.comon.weixin.common.OpenApi;
import com.modules.api.comon.weixin.entity.WeChatVO;
import com.modules.api.entity.User;
import com.modules.api.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chen
 */
@RestController
@Api(value = "LoginController", tags = {"LoginController"}, description = "登录")
public class LoginController extends BaseController {


  @Resource
  private UserService userInfoService;
  @Resource
  private JwtProperties jwtProperties;
  @Resource
  private JwtUtils jwtUtils;
  @Resource
  private RedisUtils redisUtils;


  @PostMapping(value = "/login")
  @ApiOperation("登陆/注册")
  public ResponseEntity login(@RequestBody WeChatVO weChatVO) throws SignatureException {
    String data = OpenApi.getWeixinData(weChatVO.getJsCode());
    JSONObject jsonObj = JSONObject.parseObject(data);
    User user = new User();
    if (jsonObj.containsKey("session_key")) {
      logger.info("================调微信成功=====================");
      String openId = jsonObj.get("openid").toString();
      User userInfo = userInfoService.getCountByOpenId(openId);
      if (userInfo == null) {
        user.setWechatId(openId);
        user.setHeadImg(weChatVO.getHeardImg());
        user.setNickName(weChatVO.getNickName());
        userInfoService.insert(user);
      }
    } else {
      return ResponseEntity.ok().build();
    }
    //一个用户同时只能有一台设备登录（用户端）
    String redisToken = redisUtils.getToken(user.getId());
    if (!StringUtils.isEmpty(redisToken)) {
      Claims claims = jwtUtils.getTokenClaim(redisToken);
      //判断密钥是否相等，如果不等则认为时无效的token
      if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
        throw new SignatureException(jwtProperties.header + "失效，请重新登录。");
      }
    }
    return ResponseEntity.ok(redisToken);
  }


  @PostMapping(value = "logout")
  @ApiOperation("用户退出登陆")
  public ResponseEntity logout(HttpServletRequest request) {
    redisUtils.delete(RedisUtils.ACCESS_TOKEN + getUserId(request));
    redisUtils.delete(RedisUtils.REFRESH_TOKEN + getUserId(request));
    return ResponseEntity.ok().build();
  }

  public Map<String, Object> redisLoginInfo(User user) {
    //设置单次的token的过期时间为凌晨3点-4点，用于避免token在即将失效时继续使用旧的token访问
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, +1);
    cal.set(Calendar.HOUR_OF_DAY, 3);
    //拼装accessToken
    String accessToken = jwtUtils.createToken(user.getNickName(), user.getId());
    //将该用户的access_token储存到redis服务器，保证一段时间内只能有一个有效的access_token
    redisUtils.setToken(user.getId(), accessToken, cal.getTimeInMillis() - System.currentTimeMillis());
    //获取refresh_token，有效期为7天，每次通过refresh_token获取access_token时，会刷新refresh_token的时间
    String refreshToken = jwtUtils.createToken(user.getNickName(), user.getId());
    redisUtils.setRefreshToken(user.getId(), refreshToken);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("access_token", "bearer" + accessToken);
    result.put("refresh_token", "bearer" + refreshToken);
    result.put("user", user);
    return result;
  }


  @PostMapping(value = "getToken")
  @ApiOperation("通过refreshToken获取新的access_token，同时也刷新refreshToken的有效期")
  public ResponseEntity getToken(@RequestBody TokenQuery account) {
    String refreshToken = account.getRefreshToken();
    Calendar cal = Calendar.getInstance();
    try {
      if (org.apache.commons.lang3.StringUtils.isNotEmpty(refreshToken)) {
        String HeadStr = refreshToken.substring(0, 6).toLowerCase();
        if (HeadStr.equals("bearer")) {
          refreshToken = refreshToken.substring(6);
          Claims claims = jwtUtils.getTokenClaim(refreshToken);
          //判断密钥是否相等，如果不等则认为时无效的token
          if (claims != null) {
            String username = (String) claims.get("username");
            Long userId = (Long) claims.get("userId");
            //refresh_token未失效，refresh_token需要和redis服务器中的储存的refresh_token值一样才有效
            if (refreshToken.equals(redisUtils.getRefreshToken(userId)) && account.getNickName().equals(username)) {
              User user = userInfoService.get(userId);
              Map<String, String> tokenVO = new HashMap<>();
              Map<String, Object> resultToken = redisLoginInfo(user);
              tokenVO.put("access_token", "bearer" + resultToken.get("access_token"));
              tokenVO.put("refresh_token", "bearer" + resultToken.get("refresh_token"));
              //更新redis数据
              redisUtils.delete(RedisUtils.ACCESS_TOKEN + user.getId());
              redisUtils.delete(RedisUtils.REFRESH_TOKEN + user.getId());

              redisUtils.setToken(user.getId(), resultToken.get("access_token").toString(), cal
                      .getTimeInMillis() - System.currentTimeMillis());
              redisUtils.setRefreshToken(user.getId(), resultToken.get("refresh_token").toString());
              return ResponseEntity.ok(tokenVO);
            }
          }
        }
      }
      return ResponseEntity.ok(null);
    } catch (Exception e) {
      return ResponseEntity.ok(null);
    }
  }


  //解析电话号码
  public JSONObject getPhoneNumber(String session_key, String encryptedData, String iv) {
    System.out.println(session_key);
    byte[] dataByte = org.bouncycastle.util.encoders.Base64.decode(encryptedData);
    // 加密秘钥
    byte[] keyByte = org.bouncycastle.util.encoders.Base64.decode(session_key);
    // 偏移量
    byte[] ivByte = org.bouncycastle.util.encoders.Base64.decode(iv);
    try {
      // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
      int base = 16;
      if (keyByte.length % base != 0) {
        int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
        byte[] temp = new byte[groups * base];
        Arrays.fill(temp, (byte) 0);
        System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
        keyByte = temp;
      }
      // 初始化
      Security.addProvider(new BouncyCastleProvider());
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
      SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
      AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
      parameters.init(new IvParameterSpec(ivByte));
      cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
      byte[] resultByte = cipher.doFinal(dataByte);
      if (null != resultByte && resultByte.length > 0) {
        String result = new String(resultByte, "UTF-8");
        return JSONObject.parseObject(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  @PostMapping(value = "getPhoneByWeChat")
  @ApiOperation("授权手机号")
  public ResponseEntity getPhoneByWeChat(@RequestBody PhoneQuery phoneQuery) {
    try {
      //解密电话号码
      JSONObject obj = getPhoneNumber(phoneQuery.getSessionKey(), phoneQuery.getEncryptedData(), phoneQuery.getIv());
      return ResponseEntity.ok(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

}
