package org.example.wms_backend.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户表
 */
@Data
public class User {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 账号
     */
    private String account;
    /**
     * 姓名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 权限等级
     */
    private Integer permissionLevel;
    /**
     * 账号状态
     */
    private String accountStatus;
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
