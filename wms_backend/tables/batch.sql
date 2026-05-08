CREATE TABLE batch (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `batchNo` VARCHAR(20) UNIQUE COMMENT '批次号，格式为LOT+随机三位大小写英文与数字混合字符串+当前年月如0308',
  `goodsCode` VARCHAR(20) COMMENT '商品编码',
  `productionDate` DATE COMMENT '生产日期',
  `expiryDate` DATE COMMENT '到期日期',
  `batchQuantity` INT COMMENT '批次数量',
  `remainingQuantity` INT COMMENT '剩余数量',
  `warehouseCode` VARCHAR(20) COMMENT '仓库编码',
  `status` VARCHAR(20) COMMENT '状态',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '批次表';
