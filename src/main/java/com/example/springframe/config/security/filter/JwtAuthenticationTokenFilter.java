package com.example.springframe.config.security.filter;

import cn.hutool.json.JSONUtil;
import com.example.springframe.config.jwt.JwtPropertiesConfig;
import com.example.springframe.config.jwt.JwtService;
import com.example.springframe.config.jwt.UserClaims;
import com.example.springframe.config.security.impl.UserSecurityDetailImpl;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.ResponseCode;
import com.example.springframe.license.LicenseVerify;
import com.example.springframe.utils.ReturnWrite;
import com.example.springframe.utils.cache.CaffeineUtils;
import com.example.springframe.utils.redis.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtService jwtService;

    @Resource
    private JwtPropertiesConfig jwtPropertiesConfig;

    
    @Resource
    private CaffeineUtils caffeineUtils;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Value("${spring.redis.open}")
    private boolean redisOpen;

    @Resource
    private LicenseVerify licenseVerify;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        //license校验
        if(!licenseVerify.verify()){
            chain.doFilter(request, response);
            return;
        }

        String authHeader;
        authHeader = request.getHeader(jwtPropertiesConfig.getHeader());
//        boolean isIcGov = Arrays.stream(icUris).parallel().anyMatch(e -> request.getRequestURI().equals(e));
        if (authHeader != null && authHeader.startsWith(jwtPropertiesConfig.getTokenHead())) {
            // The part after "Bearer "
            final String authToken = authHeader.substring(jwtPropertiesConfig.getTokenHead().length());
            UserClaims customizedClaims;
            try {
                customizedClaims = jwtService.parseClaimsFromToken(authToken);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException uje) {
                ReturnWrite.writeResp(response, APIResponse.fail(ResponseCode.ILLEGAL_TOKEN));
                return;
            } catch (ExpiredJwtException eje) {
                ReturnWrite.writeResp(response, APIResponse.fail(ResponseCode.TOKEN_INVALID));
                return;
            } catch (IllegalArgumentException uje) {
                ReturnWrite.writeResp(response, APIResponse.fail(ResponseCode.NOTFUND_TOKEN));
                return;
            }
            String username = customizedClaims.getUsername();
            log.info("checking authentication " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //采用redis缓存，减少数据库查询次数
//                UserDetails userDetails = (UserDetails)redisUtil.get(username);
                UserDetails userDetails;
                if(redisOpen){
                    userDetails = JSONUtil.toBean(JSONUtil.toJsonStr(redisUtil.get(username)), UserSecurityDetailImpl.class);
                    if(Objects.isNull(userDetails)){
                        userDetails = this.userDetailsService.loadUserByUsername(username);
                    }
                }else {
                    if(Objects.nonNull(caffeineUtils.getCache().getIfPresent(username))){
                        userDetails = (UserDetails)caffeineUtils.getCache().getIfPresent(username);
                    }else{
                        userDetails = this.userDetailsService.loadUserByUsername(username);
                    }
                }
//                UserDetails userDetails;
//                if(Objects.nonNull(caffeineUtils.getCache().getIfPresent(username))){
//                    userDetails = (UserDetails)caffeineUtils.getCache().getIfPresent(username);
//                }else{
//                    userDetails = this.userDetailsService.loadUserByUsername(username);
//                }


                // 如果是初始密码，返回特殊状态码。
//                if (passwordEncoder.matches(jwtPropertiesConfig.getInitPass(), userDetails.getPassword()) && !request.getRequestURI().contains("updateOwnPassword")) {
//                    writeResp(response, JSON.toJSONString(APIResponse.fail(ResponseCode.NEED_CHANGE_PAAS.getCode(), "需要更新初始密码")));
//                    return;
//                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");

                try {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //添加缓存
                    if(redisOpen){
                        redisUtil.set(username, JSONUtil.toJsonStr(userDetails),jwtPropertiesConfig.getExpiration());
                    }else {
                        caffeineUtils.getCache().put(username,userDetails);
                    }
//                    redisUtil.set(username,userDetails,jwtPropertiesConfig.getExpiration());
//                    caffeineUtils.getCache().put(username,userDetails);
                } catch (Exception e) {
                    log.error("------------异常---------" + e);
                }
            }
        }

        chain.doFilter(request, response);
    }

}
