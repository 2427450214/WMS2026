package org.example.wms_backend.entity;

import lombok.Data;

@Data
public class GoodsInboundStatistic {
    private String goodsCode;
    private String goodsName;
    private Integer totalInboundQuantity;
}