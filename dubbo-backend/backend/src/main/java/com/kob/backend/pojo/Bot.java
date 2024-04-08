package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zeroac
 */
@Data // Lombok: 自动生成getter/setter/equals/canEqual/hashCode/toString方法
@NoArgsConstructor // Lombok: 自动生成无参构造方法
@AllArgsConstructor // Lombok: 自动生成全参构造方法
@Builder // Lombok: 自动生成建造者模式代码 即 User user = User.builder().username("john_doe").password("secure_password").build();
@TableName("bot") // MyBatis-Plus: 指定数据库表名
public class Bot {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Integer rating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
