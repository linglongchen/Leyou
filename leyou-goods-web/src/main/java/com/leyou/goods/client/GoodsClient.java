package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 使用fiegnClient来获取面向接口编程风格；
 * 继承item-interface模块的goods接口，这样就不用再写一遍方法了
 *
 * @create: 2019-07-16 00:33
 **/
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
