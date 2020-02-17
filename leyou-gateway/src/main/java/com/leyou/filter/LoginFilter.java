package com.leyou.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 */
@EnableConfigurationProperties(value = {JwtProperties.class, FilterProperties.class})
@Component  //将该过滤器注册到spring容器
public class LoginFilter extends ZuulFilter {


    //注入jwt配置类
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;


    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //获取白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths();

        //初始化zuul网关的运行上下文，注意是zuul网关的
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取请求url
        String url = request.getRequestURL().toString();

        //遍历判断url中是否包含白名单
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url, allowPath)) {
                //如果包含则不作拦截
                return false;
            }
        }

        return true;
    }

    /**
     * 拦截的主要方法
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //初始化zuul网关的运行上下文，注意是zuul网关的
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();

        //获取cookie
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        /*获取token时可以不用校验，后面在校验时会再作一次校验
        if (StringUtils.isBlank(token)) {
            //zuul网关不进行转发请求
            context.setSendZuulResponse(false);
            //给浏览器响应
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }*/

        //校验
        try {
            //校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            //zuul网关不进行转发请求
            context.setSendZuulResponse(false);
            //给浏览器响应
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
