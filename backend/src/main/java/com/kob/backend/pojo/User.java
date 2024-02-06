package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zeroac
 * 使用Lombok注解自动化地生成常见的方法和构造函数。
 */
@Data // 自动生成getter、setter、toString、equals、hashCode方法
@NoArgsConstructor // 自动生成无参构造函数
@AllArgsConstructor // 自动生成包含所有参数的构造函数
public class User {
    @TableId(type = IdType.AUTO) // 设置主键自增
    private Integer id;
    private String username;
    private String password;
    private String createTime;
    private String updateTime;
    private String deleteTime;
    private Boolean isDeleted;
}
