CREATE TABLE warehouse (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `warehouseName` VARCHAR(100) COMMENT '仓库名称',
  `warehouseCode` VARCHAR(20) UNIQUE COMMENT '仓库编码，格式为WH+随机生成8位大小写英文与数字混合字符串',
  `status` VARCHAR(20) COMMENT '状态',
  `warehouseType` VARCHAR(50) COMMENT '仓库类型',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '仓库表';
