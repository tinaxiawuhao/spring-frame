package com.example.springframe.config.security.filter;

import com.example.springframe.config.jwt.JwtPropertiesConfig;
import com.example.springframe.config.jwt.JwtService;
import com.example.springframe.config.jwt.UserClaims;
import com.example.springframe.rest.RestResult;
import com.example.springframe.utils.ReturnWrite;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtService jwtService;

    @Resource
    private JwtPropertiesConfig jwtPropertiesConfig;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
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
                ReturnWrite.writeResp(response, RestResult.failed(RestResult.ResEnum.ILLEGAL_TOKEN));
                return;
            } catch (ExpiredJwtException eje) {
                ReturnWrite.writeResp(response, RestResult.failed(RestResult.ResEnum.TOKEN_INVALID));
                return;
            } catch (IllegalArgumentException uje) {
                ReturnWrite.writeResp(response, RestResult.failed(RestResult.ResEnum.NOTFUND_TOKEN));
                return;
            }
            String username = customizedClaims.getUsername();
            log.info("checking authentication " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // 如果是初始密码，返回特殊状态码。
//                if (passwordEncoder.matches(jwtPropertiesConfig.getInitPass(), userDetails.getPassword()) && !request.getRequestURI().contains("updateOwnPassword")) {
//                    writeResp(response, JSON.toJSONString(RestResult.failed(RestResult.ResEnum.NEED_CHANGE_PAAS.getCode(), "需要更新初始密码")));
//                    return;
//                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");

                try {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    log.error("------------异常---------" + e);
                }
            }
        }

        chain.doFilter(request, response);
    }

}
