package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 出库详情表
 */
@Data
public class OutboundDetail {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 出库单ID，格式为CK+随机生成8位大小写英文与数字混合字符串
     */
    private String outboundId;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
     * 出库数量
     */
    private Integer outboundQuantity;
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
