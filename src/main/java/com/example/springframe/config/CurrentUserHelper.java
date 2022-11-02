package com.example.springframe.config;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurrentUserHelper {

//    public SysUser getSysUser() {
//        // 获取当前用户
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserSecurityDetailImpl u = (UserSecurityDetailImpl) authentication.getPrincipal();
//        SysUser user = u.getUser();
//        if (user == null) {
//            throw new RuntimeException("未获取到用户");
//        }
//
//        return user;
//    }
//
//
//    public List<String> getRoles() {
//        // 获取当前用户
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserSecurityDetailImpl u = (UserSecurityDetailImpl) authentication.getPrincipal();
//        List<String> collect = u.getRoles().stream().map(SysRole::getRole).collect(Collectors.toList());
//        return collect;
//    }
//
//    public boolean hasRole(String role) {
//        List<String> roles = getRoles();
//        if (roles == null) {
//            return false;
//        } else {
//            return roles.contains(role);
//        }
//
//    }

}
