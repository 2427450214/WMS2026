package org.example.wms_backend.entity;

import lombok.Data;

/**
 * 商品出库统计实体类
 * 用于存储近一周内每个商品出库总数量
 */
@Data
public class GoodsOutboundStatistic {
    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 近一周内出库总数量
     */
    private Integer totalOutboundQuantity;
}