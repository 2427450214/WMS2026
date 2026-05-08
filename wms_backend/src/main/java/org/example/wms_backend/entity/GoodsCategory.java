package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 货物分类表
 */
@Data
public class GoodsCategory {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 分类名称
     */
    private String categoryName;
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
