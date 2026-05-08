CREATE TABLE goods (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `goodsName` VARCHAR(100) COMMENT '货物名称',
  `goodsCode` VARCHAR(20) UNIQUE COMMENT '商品编码，格式为SP+随机生成8位大小写英文与数字混合字符串',
  `categoryId` INT COMMENT '货物分类ID',
  `stockQuantity` INT COMMENT '库存数量',
  `shelfLife` INT COMMENT '保质期（天）',
  `minStockWarning` INT COMMENT '最低库存预警线',
  `maxStockWarning` INT COMMENT '最高库存预警线',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '货物表';
