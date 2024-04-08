package com.kob.backend.controller.commons;

import com.kob.backend.pojo.User;
import com.kob.backend.service.account.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * @author zeroac
 * <p>
 * 基础Control 用于继承
 */
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public abstract class BasisControl {
    protected final Logger logger;

    // 在基类中使用一个非静态的Logger变量，并在构造函数中初始化它。
    // 这样每个实例都会有自己的Logger引用，而且这个引用将指向正确的子类。
    protected BasisControl() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public User getUser() {
        // 获取当前的Authentication对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 检查authentication是否为空
        if (authentication == null) {
            // 抛出一个认证相关的异常
            throw new AuthenticationCredentialsNotFoundException("当前用户未认证。");
        }

        // 检查principal是否是UserDetailsImpl的实例
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            // 抛出一个适当的异常
            throw new AuthenticationCredentialsNotFoundException("认证信息不正确。");
        }

        // 现在我们可以安全地进行转换
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();

        // 检查loginUser是否为空
        if (loginUser == null) {
            // 抛出一个适当的异常
            throw new AuthenticationCredentialsNotFoundException("无法从认证信息中获取用户详情。");
        }

        // 返回User对象，这里假设getUser不会返回null
        // 如果getUser有可能返回null，你需要在这里也进行检查并抛出异常
        User user = loginUser.getUser();
        if (user == null) {
            // 抛出一个适当的异常
            throw new IllegalStateException("无法获取用户实体。");
        }

        return user;
    }

    public ResponseEntity<Map<String, String>> getResponse(Map<String, String> ret) {
        String errorMessage = ret.get("error_message");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            return ResponseEntity.ok(ret);
        } else {
            return ResponseEntity.status(400).body(ret);
        }
    }
}
