package com.example.springframe.utils.cache;

import com.example.springframe.config.jwt.JwtPropertiesConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Data
@Slf4j
public class CaffeineUtils {
    private Cache<String, Object> cache;

    @Resource
    private JwtPropertiesConfig jwtPropertiesConfig;

    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder()
                //初始数量
                .initialCapacity(10)
                //最大条数
                .maximumSize(1000)
                //expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
                //最后一次写操作后经过指定时间过期
                .expireAfterWrite(jwtPropertiesConfig.getExpiration(), TimeUnit.SECONDS)
                //最后一次读或写操作后经过指定时间过期
                .expireAfterAccess(jwtPropertiesConfig.getExpiration(), TimeUnit.SECONDS)
                //监听缓存被移除
                .removalListener((key, val, removalCause) -> {
                    log.info("淘汰缓存：key:{} val:{}", key, val);
                })
                //记录命中
                .recordStats()
                .build();
    }
}
