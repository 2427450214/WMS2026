CREATE TABLE inboundDetail (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `inboundId` VARCHAR(20) UNIQUE COMMENT '入库单ID，格式为RK+随机生成8位大小写英文与数字混合字符串',
  `goodsCode` VARCHAR(20) COMMENT '商品编码',
  `inboundQuantity` INT COMMENT '入库数量',
  `batchNo` VARCHAR(20) COMMENT '批次号',
  `warehouseCode` VARCHAR(20) COMMENT '仓库编码',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '入库详情表';
