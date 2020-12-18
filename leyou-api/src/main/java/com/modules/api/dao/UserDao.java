package com.modules.api.dao;

import com.modules.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @author chenTom
 * @date 2020-07-23 16:18:12
 */
public interface UserDao {

  @Select("select * from user where is_deleted = 0")
  User get(Long id);

  /**
   * 覆盖原来的接口方法设置默认自增
   * @param record
   * @return int
   * @author chenTom
   * @date 2020-07-23 16:18:12
   */
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(User record);


  /**
   * 查询该openid是否存在
   * @return
   */
  @Select("select * from user where wechat_id=#{wechatId}")
  @ResultType(Integer.class)
  User getCountByOpenId(String openId);


  @Select("select * from user where wechat_id=#{wechatId}")
  @ResultType(User.class)
  User getByOpenId(User userInfo);

}
