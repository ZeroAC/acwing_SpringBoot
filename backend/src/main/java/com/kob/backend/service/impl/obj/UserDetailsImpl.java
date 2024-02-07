package com.kob.backend.service.impl.obj;

import com.kob.backend.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 实现Spring Security的UserDetails接口，提供了基本的用户信息。
 * 这个类是用户认证信息的自定义封装，用来适配Spring Security框架。
 * 用户的认证相关属性和权限信息将在这个类中处理。
 *
 * @author zeroac
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user; // 用户实体类，包含用户的详细信息

    /**
     * 获取用户权限集合，目前未实现，返回null。
     *
     * @return 当前用户的权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 权限集合，可根据需要返回用户的权限列表
    }


    @Override
    public String getPassword() {
        return user.getPassword(); // 返回用户密码
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 返回用户的用户名
    }

    /**
     * 账号是否未过期，这里默认返回true，表示账号永不过期。
     *
     * @return 账号未过期返回true，否则返回false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户是否有效
    }

    /**
     * 账号是否未锁定，这里默认返回true，表示账号未锁定。
     *
     * @return 账号未锁定返回true，否则返回false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // 账户是否未锁定
    }

    /**
     * 凭证是否未过期，这里默认返回true，表示凭证（密码）永不过期。
     *
     * @return 凭证未过期返回true，否则返回false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证是否有效
    }

    /**
     * 账号是否可用，这里默认返回true，表示账号可用。
     *
     * @return 账号可用返回true，否则返回false
     */
    @Override
    public boolean isEnabled() {
        return true; // 账户是否启用
    }
}