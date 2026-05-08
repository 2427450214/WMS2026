package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 库存变动日志表
 */
@Data
public class StockChangeLog {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 商品编号
     */
    private String goodsCode;
    /**
     * 仓库编号
     */
    private String warehouseCode;
    /**
     * 变动前数量
     */
    private Integer beforeQuantity;
    /**
     * 变动数量
     */
    private Integer changeQuantity;
    /**
     * 变动后数量
     */
    private Integer afterQuantity;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 出入库详细表编码
     */
    private String inOutDetailCode;
    /**
     * 账号
     */
    private String account;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 操作时间
     */
    private Date operationTime;
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
