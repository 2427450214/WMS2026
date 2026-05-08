package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 仓库表
 */
@Data
public class Warehouse {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库编码，格式为WH+随机生成8位大小写英文与数字混合字符串
     */
    private String warehouseCode;
    /**
     * 状态
     */
    private String status;
    /**
     * 仓库类型
     */
    private String warehouseType;
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
