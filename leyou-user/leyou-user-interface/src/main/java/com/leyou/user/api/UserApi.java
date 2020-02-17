package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 * @create: 2019-07-19 19:09
 **/
public interface UserApi {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    User query(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);
}

