package org.example.wms_backend.task;

import org.example.wms_backend.mapper.OperationLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 操作日志清理定时任务
 */
@Component
public class OperationLogCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogCleanupTask.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 每天凌晨执行一次，删除超过三天的操作日志
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldLogs() {
        try {
            int deletedCount = operationLogMapper.deleteOldLogs();
            logger.info("清理操作日志完成，删除了 {} 条超过三天的记录", deletedCount);
        } catch (Exception e) {
            logger.error("清理操作日志失败: {}", e.getMessage());
        }
    }
}