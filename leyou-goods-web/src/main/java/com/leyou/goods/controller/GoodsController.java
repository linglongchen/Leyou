package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 商品详情页请求处理
 *
 * @create: 2019-07-17 20:10
 **/
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 根据spuId来获取商品相关的参数，并封装到页面
     *
     * @param sid
     * @param model
     * @return
     */
    @GetMapping("item/{sid}.html")
    public String toItemPage(@PathVariable("sid") Long sid, Model model) {
        Map<String, Object> data = goodsService.loadData(sid);

        model.addAllAttributes(data);

        this.goodsHtmlService.createHtml(sid);

        return "item";
    }

}
