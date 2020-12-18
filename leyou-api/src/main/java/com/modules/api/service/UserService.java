package com.modules.api.service;


import com.github.pagehelper.PageInfo;
import com.modules.api.entity.User;

import java.util.List;

/**
 * @author chenTom
 * @date 2020-07-23 16:18:12
 */
public interface UserService {
  /**
   * 获取一条数据
   * @param id
   * @return com.modules.system.entity.User
   * @author chenTom
   * @date 2020-07-23 16:18:12
   */
  User get(Long id);
  /**
   * 插入一条数据
   * @param entity
   * @return int
   * @author chenTom
   * @date 2020-07-23 16:18:12
   */
  long insert(User entity);



  /**
   * 查询该openid是否存在
   * @param openId
   * @return
   */
  public User getCountByOpenId(String openId);

  /**
   * 根据openid获取用户信息
   * @param userInfo
   * @return
   */
  public User getByOpenId(User userInfo);


}
