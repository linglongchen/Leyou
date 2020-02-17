package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Administrator
 * @create: 2019-07-19 19:13
 **/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
