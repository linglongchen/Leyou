package com.modules.api.service.impl;

import com.modules.api.dao.UserDao;
import com.modules.api.entity.User;
import com.modules.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenTom
 * @date 2020-07-23 16:18:12
 */
@Service
public class UserServiceImpl implements UserService {



  @Resource
  private UserDao userDao;

  @Override
  public User get(Long id) {
    User user = userDao.get(id);
    return user;
  }


  @Override
  public long insert(User entity) {
    int i = userDao.insert(entity);
    if (i > 0) {
      return entity.getId();
    }
    return 0;
  }


  @Override
  public User getCountByOpenId(String openId) {
    return userDao.getCountByOpenId(openId);
  }

  /**
   * 根据openid获取用户信息
   * @param userInfo
   * @return
   */
  @Override
  public User getByOpenId(User userInfo) {
    return userDao.getByOpenId(userInfo);
  }

}
