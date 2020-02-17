package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @create: 2019-07-17 21:40
 **/
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

    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> model = new HashMap<>();

        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);

        //查询分类：Map<String, Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNameByIds(cids);
        //初始化一个分类的list<map>
        List<Map<String, Object>> categories = new ArrayList<>();
        Map<String, Object> category;
        for (int i = 0; i < cids.size(); i++) {
            category = new HashMap<>();
            category.put("id", cids.get(i));
            category.put("name", names.get(i));
            categories.add(category);
        }

        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //查询sku集合
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());

        //查询特殊规格参数组
        List<SpecParam> params = this.specificationClient.querySpecParam(null, spu.getCid3(), false, null);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> paramMap.put(param.getId(), param.getName()));

        //封装参数
        //spu
        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        //sku集合
        model.put("skus", skus);
        //分类
        model.put("categories", categories);
        //品牌
        model.put("brand", brand);
        //规格参数组
        model.put("groups", groups);
        //特殊规格参数组
        model.put("paramMap", paramMap);

        return model;
    }

}
