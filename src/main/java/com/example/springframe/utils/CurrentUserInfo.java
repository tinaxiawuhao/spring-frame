package com.example.springframe.utils;

import com.example.springframe.config.security.impl.UserSecurityDetailImpl;
import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrentUserInfo {

    public SysUser getSysUser() {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDetailImpl u = (UserSecurityDetailImpl) authentication.getPrincipal();
        SysUser user = u.getUser();
        if (user == null) {
            throw new RuntimeException("未获取到用户");
        }

        return user;
    }


    public List<SysRole> getRoles() {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDetailImpl u = (UserSecurityDetailImpl) authentication.getPrincipal();
        return u.getRoles();
    }

    public boolean hasRole(String role) {
        List<SysRole> roles = getRoles();
        List<String> collect = roles.stream().map(SysRole::getRole).collect(Collectors.toList());
        return collect.contains(role);

    }
}
