package com.example.springframe.config.jwt;

import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.SysUser;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 整合security和jwt封装claims
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserClaims extends DefaultClaims{

    private String username;

    private String userId;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 封装时创建， 做两件事：
     * 1.初始化封装JWT的声明参数
     * 2.把属性丢到jwt DefaultClaims中去（deep方法）
     *
     * @param user 用户信息
     * @param roles 角色列表
     */
    public UserClaims(SysUser user, List<SysRole> roles) {
        if (user == null) {
            throw new RuntimeException("user can not be null");
        }
        this.username = user.getUsername();
        this.userId = String.valueOf(user.getId());
        if (roles != null) {
            this.authorities = roles.stream()
                    .map(e -> new SimpleGrantedAuthority(e.getRole()))
                    .collect(Collectors.toList());
        }
        initClaims();
        deep();
    }

    private void initClaims() {
        // jwt签发时间
        super.setIssuedAt(new Date());
//        // jwt过期时间，必须大于签发时间
        super.setExpiration(new Date(System.currentTimeMillis() + 7200000));
        // jwt所面向的用户
        super.setSubject(username);
        // jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
        super.setId(UUID.randomUUID().toString());
        // jwt的签发时间
        super.setIssuer("demo.com");
        // 接收jwt的一方
//        super.setAudience();
    }

    /**
     * 从token解析出来DefaultClaims，再根据username从数据库获取user和roles信息
     * @param defaultClaims
     */
    public UserClaims(Map<String, Object> defaultClaims) {
        super(defaultClaims);
        dig();
    }

    /**
     * 把父类map中的属性，赋值给UserClaims
     */
    private void dig(){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(this, get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把UserClaims封装的属性初始化到父类map中去
     */
    private void deep() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object o = field.get(this);
                setValue(field.getName(), o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
