package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @create: 2019-07-18 19:19
 **/
@Component      //注册到spring容器中
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;


    /**
     * 页面静态化新增及更新监听
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(   //开启监听并声名绑定
            //绑定队列，队列的名称，持久化
            value = @Queue(value = "LEYOU.ITEM.SAVE.QUEUE", durable = "true"),
            //绑定交换器，忽略声名异常，发布订阅的模式为tpoic
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //通配符
            key = {"item.insert", "item.update"}
    ))
    public void save(Long id) {
        if (id == null) {
            return;
        }

        goodsHtmlService.createHtml(id);
    }

    /**
     * 页面静态化删除监听
     *
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(   //开启监听并声名绑定
            //绑定队列，队列的名称，持久化
            value = @Queue(value = "LEYOU.ITEM.DELETE.QUEUE", durable = "true"),
            //绑定交换器，忽略声名异常，发布订阅的模式为tpoic
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //通配符
            key = {"item.delete"}
    ))
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        goodsHtmlService.deleteHtml(id);
    }
}
