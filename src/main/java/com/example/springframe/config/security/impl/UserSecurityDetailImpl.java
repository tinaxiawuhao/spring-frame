package com.example.springframe.config.security.impl;

import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户安全信息的封装
 * 非spring管理，
 * 由SpringSecurity的接口UserDetailsService负责根据用户名获取数据源信息后创建
 */
@Data
@AllArgsConstructor
public class UserSecurityDetailImpl implements UserDetails{

    private SysUser user;

    private List<SysRole> roles;

    /**
     * 获取角色
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(e -> new SimpleGrantedAuthority(e.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getPassword();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
