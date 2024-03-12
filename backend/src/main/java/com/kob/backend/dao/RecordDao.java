package com.kob.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.service.pk.model.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zeroac
 */
@Mapper
public interface RecordDao extends BaseMapper<Record> {
}
