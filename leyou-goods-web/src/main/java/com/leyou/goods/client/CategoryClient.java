package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @create: 2019-07-12 13:31
 **/
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
