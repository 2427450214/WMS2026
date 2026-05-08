package org.example.wms_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

/**
 * Redis连接健康检查组件
 * <p>用于在应用启动时检查Redis连接状态</p>
 */
@Component
public class RedisHealthChecker implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RedisHealthChecker.class);

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void run(String... args) throws Exception {
        logger.info("正在检查Redis连接状态...");
        try {
            // 尝试获取Redis连接
            redisConnectionFactory.getConnection();
            logger.info("Redis连接成功");
        } catch (Exception e) {
            logger.error("Redis未开启或连接失败: {}", e.getMessage());
            System.err.println("redis未开启");
            // 关闭应用
            System.exit(1);
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("应用关闭，清理Redis连接");
    }
}