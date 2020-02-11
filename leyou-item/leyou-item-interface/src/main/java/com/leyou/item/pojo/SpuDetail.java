package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String genericSpec;
    private String specialSpec;
    private String description;// 商品描述
    private String packingList;// 包装清单
    private String afterService;// 售后服务
    // 省略getter和setter
}
