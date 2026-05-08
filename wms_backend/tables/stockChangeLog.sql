CREATE TABLE stockChangeLog (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `goodsCode` VARCHAR(20) COMMENT '商品编号',
  `warehouseCode` VARCHAR(20) COMMENT '仓库编号',
  `beforeQuantity` INT COMMENT '变动前数量',
  `changeQuantity` INT COMMENT '变动数量',
  `afterQuantity` INT COMMENT '变动后数量',
  `operationType` VARCHAR(20) COMMENT '操作类型',
  `inOutDetailCode` VARCHAR(20) COMMENT '出入库详细表编码',
  `account` VARCHAR(20) COMMENT '账号',
  `userName` VARCHAR(20) COMMENT '用户名称',
  `operationTime` DATETIME COMMENT '操作时间',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除属性，0表示未删除，1表示已删除'
) COMMENT '库存变动日志表';
