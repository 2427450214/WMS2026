package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 批次表
 */
@Data
public class Batch {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 批次号，格式为LOT+随机三位大小写英文与数字混合字符串+当前年月如0308
     */
    private String batchNo;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
     * 生产日期
     */
    private Date productionDate;
    /**
     * 到期日期
     */
    private Date expiryDate;
    /**
     * 批次数量
     */
    private Integer batchQuantity;
    /**
     * 剩余数量
     */
    private Integer remainingQuantity;
    /**
     * 仓库编码
     */
    private String warehouseCode;
    /**
     * 状态
     */
    private String status;
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
