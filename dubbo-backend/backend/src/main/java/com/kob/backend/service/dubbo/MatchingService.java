package com.kob.backend.service.dubbo;

import com.kob.matching.api.MatchingApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @author zeroac
 * 在 WebSocket 中直接使用 @DubboReference 注解注入可能会导致注入为空的问题。
 * 这是因为 WebSocket 的实例化和生命周期管理通常由 WebSocket 容器负责，
 * 而不是由 Spring 容器管理。
 * 为了解决这个问题，创建一个中间服务类，用于封装 Dubbo 服务的调用
 * 在这个中间服务类中，使用 @DubboReference 注解注入 MatchingApi 接口，
 * 并提供一些方法来封装 Dubbo 服务的调用逻辑
 */
@Service
public class MatchingService {
    @DubboReference
    private MatchingApi matchingApi;

    public void addPlayer(Integer userId, Integer rating) {
        matchingApi.addPlayer(userId, rating);
    }

    public void removePlayer(Integer userId) {
        matchingApi.removePlayer(userId);
    }
}
