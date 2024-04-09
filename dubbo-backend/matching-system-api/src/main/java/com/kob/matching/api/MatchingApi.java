package com.kob.matching.api;

/**
 * @author zeroac
 *
 * 匹配系统微服务接口
 */
public interface MatchingApi {
    //匹配系统中添加一个用户
    String addPlayer(Integer userId, Integer rating);
    //匹配系统中删除一个用户
    String removePlayer(Integer userId);
}
