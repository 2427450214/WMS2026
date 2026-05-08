CREATE TABLE operationLog (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `account` VARCHAR(20) COMMENT '用户账号',
  `operationType` VARCHAR(10) COMMENT '操作类型（增删改）',
  `operationDetail` TEXT COMMENT '操作详细（其余每个表单）',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '操作日志表';
