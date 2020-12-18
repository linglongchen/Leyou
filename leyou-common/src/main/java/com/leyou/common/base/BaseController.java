
package com.leyou.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public abstract class BaseController {

  /**
   * 日志对象
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 获取用户id
   * @param request
   * @return
   */
  protected Integer getUserId(HttpServletRequest request) {
    return (Integer) request.getAttribute("userId");
  }

}
