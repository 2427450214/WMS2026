package org.example.wms_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * 会话配置类
 * 简化配置，不使用Spring Session，避免类型不匹配问题
 */
@Configuration
public class SessionConfig {

    /**
     * 配置HTTP会话事件发布器
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
