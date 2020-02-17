package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @create: 2019-07-12 15:40
 **/
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
