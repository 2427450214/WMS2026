CREATE TABLE system_settings (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `settingKey` VARCHAR(100) UNIQUE NOT NULL COMMENT '设置键',
  `settingValue` TEXT COMMENT '设置值（JSON格式）',
  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT '系统设置表';

-- 插入默认配置
INSERT INTO system_settings (`settingKey`, `settingValue`) 
VALUES ('homepage:components', '["dailyInbound", "expiredWarning", "topStock", "dailyOutbound"]');
