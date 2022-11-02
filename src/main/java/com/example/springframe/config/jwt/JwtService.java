package com.example.springframe.config.jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    @Resource
    private JwtPropertiesConfig jwtPropertiesConfig;


    public String generateToken(UserClaims userClaims) {
        userClaims.setExpiration(new Date(System.currentTimeMillis() + jwtPropertiesConfig.getExpiration() * 1000L));
        return Jwts.builder()
                .setClaims(userClaims)
                .signWith(SignatureAlgorithm.HS256, Base64.encodeBase64URLSafeString(jwtPropertiesConfig.getSecret().getBytes()))
                .compact();
    }

    public UserClaims parseClaimsFromToken(String token) {
        if (StringUtils.isEmpty(jwtPropertiesConfig.getSecret())) {
            String secretKey = "jwt.secret";
            log.error(secretKey + " not set.");
            throw new IllegalStateException(secretKey + " not set.");
        }

        Claims claims = Jwts
                .parser()
                .setSigningKey(Base64.encodeBase64URLSafeString(jwtPropertiesConfig.getSecret().getBytes()))
                .parseClaimsJws(token)
                .getBody();
        UserClaims userClaims = new UserClaims(claims);
        log.info("解析 -> {}", JSON.toJSONString(userClaims));
        return userClaims;
    }

    public static void main(String[] args) {
//        JWTUtils jwtUtils = new JWTUtils();
//        String liuyafei = jwtUtils.generateToken(123L, "liuyafei", "10.138.147.34");
//        System.out.println(liuyafei);
//
//        CustomizedClaims claims = jwtUtils.parseClaimsFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIiLCJqdGkiOiJiZDA1ZjI5MC1iNmE0LTRkNTUtODQ3Yy02Mjc2NzA3MjlkZjgiLCJ1c2VySWQiOiI5YTE2MjlkMi05YWVmLTRiZGUtODJiOC05ZDU4OGFmZGUwOTQiLCJpcCI6IjEwLjEzOC4xNDcuMzQiLCJpYXQiOjE1NzYxMzQ2MTAsImlzcyI6ImhhaWVyLmNvbSIsImV4cCI6MTU3NjczOTQxMH0.i82X42UQjJNyQBJO9YjZQziS5QOTxE_1tjEPdIf8L_Q");
//        System.out.println(claims.getUserId());
    }
}
