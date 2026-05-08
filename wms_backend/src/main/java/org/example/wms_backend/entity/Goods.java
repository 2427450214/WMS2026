package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 货物表
 */
@Data
public class Goods {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 货物名称
     */
    private String goodsName;
    /**
     * 商品编码，格式为SP+随机生成8位大小写英文与数字混合字符串
     */
    private String goodsCode;
    /**
     * 货物分类ID
     */
    private Integer categoryId;
    /**
     * 库存数量
     */
    private Integer stockQuantity;
    /**
     * 保质期（天）
     */
    private Integer shelfLife;
    /**
     * 最低库存预警线
     */
    private Integer minStockWarning;
    /**
     * 最高库存预警线
     */
    private Integer maxStockWarning;
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
