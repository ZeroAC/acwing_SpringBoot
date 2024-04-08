package com.kob.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 这是用户数据访问对象（DAO）的接口，定义了对用户表的操作。
 * 它继承自MyBatis Plus的BaseMapper，这样就提供了大量的通用CRUD方法。
 * 使用MyBatis时，接口和XML映射文件共同工作以执行SQL语句。
 *
 * @author zeroac
 */
@Mapper // 这个注解标识这是一个MyBatis的Mapper接口，Spring将会扫描它并创建代理对象
public interface UserDao extends BaseMapper<User> {
    // 继承了BaseMapper之后，UserDao接口会自动拥有针对User实体类的基本CRUD操作，
    // 例如：insert、delete、update、select等。
    // 你可以在这里添加更多的自定义查询方法。

    // 软删除
    @Update("UPDATE user SET is_deleted=1 WHERE id=#{userId} AND is_deleted = 0")
    int softDeleteById(int userId);

    @Select("SELECT * FROM user WHERE username=#{username} AND is_deleted = 0")
    User selectByUsername(String username);

    // 自定义查询用户方法，通过id查询并且排除已软删除的用户
    @Select("SELECT * FROM user WHERE id=#{userId} AND is_deleted = 0")
    User selectById(int userId);

    // 如果您想要返回用户列表，可以修改返回类型为 List<User>
    @Select("SELECT * FROM user WHERE is_deleted = 0")
    List<User> selectAllList();
}