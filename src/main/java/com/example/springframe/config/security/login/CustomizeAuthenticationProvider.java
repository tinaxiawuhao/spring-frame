package com.example.springframe.config.security.login;

import com.example.springframe.config.security.impl.UserSecurityDetailImpl;
import com.example.springframe.enumeration.State;
import com.example.springframe.exception.basic.ResponseCode;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * 对用户名和密码进行验证
 *
 * @author Administrator
 */
@Service
public class CustomizeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication){
        //获取前端传过来的的username和password
        String username = authentication.getName();
        String prepassword = (String) authentication.getCredentials();

        //根据用户名获取用户信息
        UserSecurityDetailImpl userDetails = (UserSecurityDetailImpl)this.userDetailsService.loadUserByUsername(username);

        if(State.DISABLED.code.equals(userDetails.getUser().getState())){
            throw new LockedException(ResponseCode.ACCOUNT_LOCKED.getMsg());
        }

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(ResponseCode.LOGIN_INVALID.getMsg());
            //获取前端传过来的密码，经过加密之后和数据库保存的密码比较是否一致
        } else if (!prepassword.equals(userDetails.getUser().getPassword()) && !DigestUtils.md5DigestAsHex(prepassword.getBytes()).equals(userDetails.getUser().getPassword())) {
            throw new BadCredentialsException(ResponseCode.LOGIN_INVALID.getMsg());
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        //返回UsernamePasswordAuthenticationToken对象
        return auth;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
