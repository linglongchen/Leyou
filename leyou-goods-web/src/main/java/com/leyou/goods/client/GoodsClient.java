package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 */
@FeignClient("item-service")
@RequestMapping("/goods")
public interface GoodsClient extends GoodsApi {

}
