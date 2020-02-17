package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @create: 2019-07-13 19:36
 **/
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
