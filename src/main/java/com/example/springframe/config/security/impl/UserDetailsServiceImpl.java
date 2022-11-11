package com.example.springframe.config.security.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.SysRoleUser;
import com.example.springframe.entity.SysUser;
import com.example.springframe.exception.basic.ResponseCode;
import com.example.springframe.mapper.SysRoleMapper;
import com.example.springframe.mapper.SysRoleUserMapper;
import com.example.springframe.mapper.SysUserMapper;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private  SysUserMapper userMapper;

    @Resource
    private SysRoleUserMapper roleUserMapper;

    @Resource
    private  SysRoleMapper roleMapper;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, s));
        if (user == null) {
            throw new InternalAuthenticationServiceException(ResponseCode.ACCOUNT_NOTFOUND.getMsg());
        }

        List<SysRoleUser> appUserRoles = roleUserMapper.selectList(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, user.getId()));
        UserSecurityDetailImpl userSecurityDetailImpl;
        if (appUserRoles == null || appUserRoles.isEmpty()){
            userSecurityDetailImpl = new UserSecurityDetailImpl(user, Collections.emptyList());
        }else {
            List<SysRole> roles = roleMapper.selectList(
                    new LambdaQueryWrapper<SysRole>().in(SysRole::getId, appUserRoles.stream()
                            .map(SysRoleUser::getRoleId).collect(Collectors.toSet())));
            userSecurityDetailImpl = new UserSecurityDetailImpl(user, roles);
        }
        return userSecurityDetailImpl;
    }
}
