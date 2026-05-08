package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志表
 */
@Data
public class OperationLog {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 操作类型（增删改）
     */
    private String operationType;
    /**
     * 操作详细（其余每个表单）
     */
    private String operationDetail;
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