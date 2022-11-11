package com.example.springframe.config.log;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理请求唯一标记字段
 */
public class RequestIdFilter {
    /**
     * Spring Security 过滤链有 bug ，
     * {@link HeaderWriter} 可能在 {@link ExceptionTranslationFilter} 后面执行，
     * 所以这里需要在 {@link ExceptionTranslationFilter} 判断是否有执行过 {@link HeaderWriter} ，
     * <br>
     * 完整逻辑说明：
     * {@link HeaderWriter} 和 {@link ExceptionTranslationFilter} 谁先执行，就将 TL.set(true) ，
     * 等到后执行的做清除操作；也就是说谁 TL.get() == true，谁就需要性清除操作
     */
    private final ThreadLocal<Boolean> TL = ThreadLocal.withInitial(() -> false);
    /**
     * 登录和退出请求时，Spring Security 过滤链到 {@link HeaderWriter} 就结束了，这里需要单独判断
     */
    private final Set<String> urls = new HashSet<String>(2) {{
        add("/login");
        add("/logout");
    }};

    /**
     * 将请求唯一标记写入响应头
     *
     * @param req {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     */
    public void writeHeaders(HttpServletRequest req, HttpServletResponse res) {
        if (urls.contains(req.getRequestURI())) {
            // 登录和退出操作过滤链到这里就结束了，所以需要在这里移除 ThreadLocal 数据
            res.addHeader("rid", RequestId.getAndRemove());
        } else {
            res.addHeader("rid", RequestId.get());
            if (TL.get()) {
                RequestId.remove();
                TL.remove();
            } else {
                TL.set(true); // 如果 {@link HeaderWriter} 已经执行，则需要设置为 true
            }
        }
    }

    public void setRequestIdFilter(HttpSecurity http) {
        http.addFilterBefore((req, res, chain) -> {
            RequestId.setRandomAlphanumeric(); // 设置请求唯一标记
            chain.doFilter(req, res);
        }, ChannelProcessingFilter.class);
        //    }, SecurityContextPersistenceFilter.class);
        http.addFilterAfter((req, res, chain) -> {
            if (TL.get()) { // 如果  {@link HeaderWriter} 已经执行过了
                RequestId.remove(); // 清除请求唯一标记
                TL.remove();
            } else {
                TL.set(true); // 如果 {@link HeaderWriter} 没执行，则需要设置为 true
            }
            chain.doFilter(req, res);
        }, ExceptionTranslationFilter.class);
    }
}
