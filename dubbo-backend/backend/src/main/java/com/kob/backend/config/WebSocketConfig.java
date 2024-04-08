package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * WebSocket配置类。
 * 使用@Configuration注解标记，表明这是一个Spring配置类，它能够创建和管理Spring应用上下文中的bean。
 * 这里定义了一个ServerEndpointExporter的bean，这个bean在使用基于注解的WebSocket时非常关键。
 *
 * @author zeroac
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {
    /**
     * 声明ServerEndpointExporter类型的bean
     * ServerEndpointExporter负责自动注册使用@ServerEndpoint注解的WebSocket端点。
     * 只有在使用Spring Boot的内嵌servlet容器时才需要显式声明这个bean。
     * 当部署到外部容器时，通常不需要提供此bean，因为外部容器将负责注册WebSocket端点。
     *
     * @return ServerEndpointExporter实例
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}
