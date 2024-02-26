package com.kob.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Bot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BotDao extends BaseMapper<Bot> {
}
