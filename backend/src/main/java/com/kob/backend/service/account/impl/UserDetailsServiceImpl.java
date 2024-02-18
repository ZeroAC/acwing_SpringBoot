package com.kob.backend.service.account.impl;

import com.kob.backend.dao.UserDao;
import com.kob.backend.pojo.User;
import com.kob.backend.service.account.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService的实现类，用于根据用户名加载用户的详细信息。
 * 在Spring Security中，这用于在认证过程中获取用户的认证信息。
 *
 * @author zeroac
 */
@Service
@RequiredArgsConstructor // 自动为所有final字段生成构造函数
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    /**
     * 根据用户名，从数据库加载用户对象，并转换为UserDetails对象返回。
     * 如果找不到用户名对应的用户，将抛出UsernameNotFoundException。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsImpl(user);
    }
}
