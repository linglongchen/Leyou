package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @create: 2019-07-18 02:22
 **/
@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private GoodsService goodsService;

    /**
     * 使用thymeleaf来创建静态html页面
     *
     * @param spuId
     */
    public void createHtml(Long spuId) {
        //静态化后页面的保存地址
        File file = new File("F:\\nginx\\nginx-1.14.2\\html\\item\\" + spuId + ".html");

        //创建输出流
        try (PrintWriter printWriter = new PrintWriter(file)) {
            //获取页面数据
            Map<String, Object> data = goodsService.loadData(spuId);

            //创建thymeleaf上下文对象
            Context context = new Context();
            //把数据放入上下文对象
            context.setVariables(data);

            //执行页面静态化方法
            templateEngine.process("item", context, printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除生成的静态页面
     *
     * @param id
     */
    public void deleteHtml(Long id) {
        File file = new File("F:\\nginx\\nginx-1.14.2\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}
