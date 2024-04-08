package com.kob.backend.controller.user.account;

import com.kob.backend.controller.commons.BasisControl;
import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

/**
 * @author zeroac
 * <p>
 * 教学CRUD演示
 */
@RestController
@RequestMapping("/user") // 基础URL
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class UserController extends BasisControl {
    /*
     * 在Spring中，字段注入（通过 @Autowired 直接在字段上）被认为是一种不佳的实践，主要是
     * 因为它违背了依赖注入的几个关键原则，如不可变性和易测试性。字段注入使得你的类和Spring框架
     * 耦合过于紧密，并且很难进行单元测试，因为你不能在不使用Spring的情况下轻松地注入依赖项。为了
     * 解决这个问题，你应该使用构造器注入或者setter注入。构造器注入是推荐的方式，因为它允许你将依
     * 赖项声明为final，这意味着一旦构造完对象，这些依赖项就不能再被更改
     * */
    private final UserDao userDao;

    public Date getNowTime() {
        return new Date();
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        // 将用户列表封装在ResponseEntity中，并设置HTTP状态码为200 OK，
        // ResponseEntity.ok()是快速创建ResponseEntity的便捷方法
        return ResponseEntity.ok(userDao.selectAllList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId) {
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/add/{username}/{password}")
    public String addUser(
            @PathVariable String username,
            @PathVariable String password) {
        String passwordEncode = new BCryptPasswordEncoder().encode(password);
        User user = new User(null, username, passwordEncode, null, getNowTime(), getNowTime(), null, false);
        userDao.insert(user);
        return String.format("Add User %s Successfully", user.getUsername());
    }

    // 删除用户
    @GetMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        try {
            // 尝试更新is_deleted字段来标记用户已被删除
            int result = userDao.softDeleteById(userId);
            if (result == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            return ResponseEntity.ok(String.format("Delete User %s Successfully", userId));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
