package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @create: 2019-07-19 18:48
 **/
@Controller
@EnableConfigurationProperties(JwtProperties.class)     //启用资源配置类，并指定类
@Api(value = "LoginController", tags = {"LoginController"}, description = "登录")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 根据用户名及密码判断用户是否登录，并且根据配置生成jwt的token返回给浏览器
     *
     * @param request
     * @param response
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
//        String token = this.authService.accredit(username, password);

//        if (StringUtils.isBlank(token)) {
//            //返回身份未认证
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        //使用工具类，设置cookie并返回给浏览器，需要cookie名称，cookie的值，过期时间，配置的是分，默认使用的秒，注意乘以60
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), "", jwtProperties.getExpire() * 60);

        return ResponseEntity.ok().build();
    }

//    /**
//     * 来解析浏览器的cookie，获取当前登录用户
//     *
//     * @return
//     */
//    @GetMapping("verify")
//    public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            //通过jwt工具类，获取用户
//            UserInfo userInfo = JwtUtils.getInfoFromToken(request, jwtProperties.getPublicKey());
//            if (userInfo == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//
//            //刷新jwt中的有效时间
//            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
//
//            //刷新cookie中的有效时间，重新再赋值一个cookie
//            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getExpire() * 60);
//
//            return ResponseEntity.ok(userInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }

}
