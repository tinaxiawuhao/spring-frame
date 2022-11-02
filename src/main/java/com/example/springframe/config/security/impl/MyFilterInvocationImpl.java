package com.example.springframe.config.security.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springframe.entity.SysPermission;
import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.mapper.SysPermissionMapper;
import com.example.springframe.mapper.SysRoleMapper;
import com.example.springframe.mapper.SysRolePermissionMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class MyFilterInvocationImpl implements FilterInvocationSecurityMetadataSource {

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        SysPermission sysPermission = sysPermissionMapper.selectOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getUrl, requestUrl));
        if(Objects.nonNull(sysPermission)){
            List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getPermissionId, sysPermission.getId()));
            List<Integer> collect = sysRolePermissions.stream().map(SysRolePermission::getRoleId).collect(Collectors.toList());
            return SecurityConfig.createList(roleMapper.selectList(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, collect))
                    .stream().map(SysRole::getRole).distinct().toArray(String[]::new));
        }

        //- 如果请求路径和数据库的所有路径都不匹配，说明这个资源是登录后即可访问的
        //- 用户登录即可访问,相当于在SecurityConfig中配置了.anyRequest().authenticated()
        return SecurityConfig.createList("ROLE_LOGIN");
    }
  
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
  
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}