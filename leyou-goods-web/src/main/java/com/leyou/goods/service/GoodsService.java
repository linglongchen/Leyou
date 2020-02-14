package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Administrator
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public Map<String,Object> loadData(Long spuId){

        try {
            // 查询spu
            Spu spu = this.goodsClient.querySpuById(spuId);

            // 查询spu详情
            SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);

            // 查询sku
            List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

            // 查询品牌
            Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

            List<Long> cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
            // 查询分类
            List<String> names = this.categoryClient.queryNameByIds(cids);
            //初始化一个分类map
            List<Map<String,Object>> categories = new ArrayList<>();
            for (int i=0;i<cids.size();i++){
                Map<String,Object> map = new HashMap<>();
                map.put("id",cids.get(i));
                map.put("name",names.get(i));
                categories.add(map);
            }

            // 查询组内参数
            List<SpecGroup> specGroups = this.specificationClient.querySpecsByCid(spu.getCid3());

            // 查询所有特有规格参数
            List<SpecParam> specParams = this.specificationClient.querySpecParam(null, spu.getCid3(), null, false);
            // 处理规格参数
            Map<Long, String> paramMap = new HashMap<>();
            specParams.forEach(param->{
                paramMap.put(param.getId(), param.getName());
            });

            Map<String, Object> map = new HashMap<>();
            map.put("spu", spu);
            map.put("spuDetail", spuDetail);
            map.put("skus", skus);
            map.put("brand", brand);
            map.put("categories", categories);
            map.put("groups", specGroups);
            map.put("params", paramMap);
            return map;
        } catch (Exception e) {
            logger.error("加载商品数据出错,spuId:{}", spuId, e);
        }
        return null;
    }
}
