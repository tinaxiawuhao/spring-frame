package com.example.springframe.config.security;

import com.example.springframe.config.jwt.JwtPropertiesConfig;
import com.example.springframe.config.security.filter.JwtAuthenticationTokenFilter;
import com.example.springframe.config.security.filter.MyUsernamePasswordAuthenticationFilter;
import com.example.springframe.config.security.handler.*;
import com.example.springframe.config.security.impl.MyDecisionManagerImpl;
import com.example.springframe.config.security.impl.MyFilterInvocationImpl;
import com.example.springframe.config.security.login.CustomizeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 自定义权限校验
    @Autowired
    private MyDecisionManagerImpl myDecisionManager;

    // 刷新菜单权限
    @Autowired
    private MyFilterInvocationImpl myFilterInvocation;

    // jwt登录验证
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // jwt配置参数
    @Resource
    private  JwtPropertiesConfig jwtPropertiesConfig;

    //注入登录成功的处理器
    @Bean
    public LoginSuccessHandler getLoginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    //注入登录失败的处理器
    @Bean
    public LoginFailHandler getLoginFailHandler() {
        return new LoginFailHandler();
    }

    //注入登出成功的处理器
    @Bean
    public CustomizeLogoutSuccessHandler getCustomizeLogOutSuccessHandler() {
        return new CustomizeLogoutSuccessHandler();
    }

    //注入未登录的异常处理器
    @Bean
    public CustomizeAuthenticationEntryPoint getCustomizeAuthenticationEntryPoint() {
        return new CustomizeAuthenticationEntryPoint();
    }

    //注入认证管理器
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //登录后,访问没有权限处理类
    @Resource
    private CustomizeAccessDeniedHandler customAccessDeniedHandler;

    //自定义登录验证
    @Autowired
    private CustomizeAuthenticationProvider customizeAuthenticationProvider;

    @Bean
    public MyUsernamePasswordAuthenticationFilter userAuthenticationFilterBean() throws Exception {
        MyUsernamePasswordAuthenticationFilter userAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        // 设置successHandler
        userAuthenticationFilter.setAuthenticationSuccessHandler(getLoginSuccessHandler());
        userAuthenticationFilter.setAuthenticationFailureHandler(getLoginFailHandler());
        return userAuthenticationFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] staticSourceArr = jwtPropertiesConfig.getStaticSources().toArray(new String[0]);
        String[] pubUrlArr = jwtPropertiesConfig.getPubUris().toArray(new String[0]);
        //1.配置授权方式，这个configure方法里面主要是配置一些
        //http的相关配置，包括登入，登出，异常处理，会话管理等
        http.authorizeRequests()
                //判断接口细粒度
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setAccessDecisionManager(myDecisionManager);
//                        object.setSecurityMetadataSource(myFilterInvocation);
//                        return object;
//                    }
//                })

                //登陆页面，登陆请求，登出请求，任何人都可以访问
//                .antMatchers("*/login", "/logout").permitAll()
                // 静态资源可以匿名访问
                .antMatchers(HttpMethod.GET, staticSourceArr).permitAll()
                // 接口可以匿名访问
                .antMatchers(pubUrlArr).permitAll()
                //动态加载权限时
//				.antMatchers("/user/getUsers").hasAnyAuthority("default","admin")
//				.antMatchers("/**").hasAuthority("admin")
                .anyRequest().authenticated().and()//其他所有请求都要认证
            .formLogin()
                .permitAll() //允许所有人访问
                .loginProcessingUrl("/login") //处理登录的url
                .successHandler(getLoginSuccessHandler()) //登录成功后，调用成功处理器
                .failureHandler(getLoginFailHandler()).and() //登录失败，//调用失败处理器
                //登出
            .logout()
                .permitAll()
                .logoutSuccessUrl("/logout")//处理登出的url
                .logoutSuccessHandler(getCustomizeLogOutSuccessHandler()) //登出成功后，调用登出成功处理器
                .deleteCookies("JSESSIONID").and()//登出之后删除cookie
            .headers().and()
                //异常处理（权限拒绝，登录失效）
            .exceptionHandling()
                //匿名用户访问无权限资源时的异常处理
                .authenticationEntryPoint(getCustomizeAuthenticationEntryPoint()) //异常处理器
                //登录后,访问没有权限处理类
                .accessDeniedHandler(customAccessDeniedHandler).and()
            .httpBasic().and()
                // 关闭CSRF跨域
            .csrf().disable()
                // 设置session策略，无状态模式
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //添加登录body参数解析
                //添加登录body参数解析
                .addFilterBefore(userAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, BasicAuthenticationFilter.class)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        //使用自定义的认证管理器
        auth.authenticationProvider(this.customizeAuthenticationProvider);
    }

}
