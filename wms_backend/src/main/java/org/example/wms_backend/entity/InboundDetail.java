package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 入库详情表
 */
@Data
public class InboundDetail {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 入库单ID，格式为RK+随机生成8位大小写英文与数字混合字符串
     */
    private String inboundId;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
     * 入库数量
     */
    private Integer inboundQuantity;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 逻辑删除属性，0表示未删除，1表示已删除
     */
    private Integer isDeleted;
}
