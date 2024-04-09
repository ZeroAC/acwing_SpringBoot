package com.kob.matching.api.impl;

import com.kob.matching.api.MatchingApi;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author zeroac
 */
@DubboService
public class MatchingApiImpl implements MatchingApi {
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player: " + userId + " " + rating);
        return "add player successfully";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        return "remove player successfully";
    }
}
