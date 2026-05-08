package org.example.wms_backend.dto;

import lombok.Data;

import java.util.Date;

/**
 * 操作日志表DTO
 */
@Data
public class OperationLogDTO {
    /**
     * 操作日志ID
     */
    private Long id;

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
}
