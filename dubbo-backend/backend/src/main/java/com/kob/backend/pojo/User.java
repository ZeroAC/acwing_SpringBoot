package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zeroac
 */
@Data // Lombok: 自动生成getter/setter/equals/canEqual/hashCode/toString方法
@NoArgsConstructor // Lombok: 自动生成无参构造方法
@AllArgsConstructor // Lombok: 自动生成全参构造方法
@Builder // Lombok: 自动生成建造者模式代码 即 User user = User.builder().username("john_doe").password("secure_password").build();
@TableName("user") // MyBatis-Plus: 指定数据库表名
// 注意 数据校验只能加了@Vaild后作为参数传入时可以使用
public class User implements Serializable {
    @TableId(type = IdType.AUTO) // MyBatis-Plus: 主键自增
    private Integer id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;


    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    private String photo;

    @TableField("create_time") // 若字段名与列名不一致，需要指定 MyBatis-Plus: 指定字段映射数据库列名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    private Boolean isDeleted;
}