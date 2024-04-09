package com.kob.matching;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zeroac
 * 在这个启动类中，配置了一个 MatchingApplication 去
 * 读取我们前面定义的 application.yml 配置文件并启动dubbo应用
 */
@SpringBootApplication
@EnableDubbo
public class MatchingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingApplication.class, args);
    }
}
