package com.example.springframe.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Configuration
public class CustomCaffeineConfig {

    /**
     * 加载 Caffeine 配置
     * @return
     */
    @Bean(name = "caffeineProperties")
    @ConfigurationProperties(prefix = "custom-caffeine.specs")
    public Map<String,String> caffeineProperties(){
        return new HashMap(16);
    }

    /**
     * 自定义 CacheManager
     * @param properties
     * @return
     */
    @Bean
    @Primary
    public CacheManager caffeineManager(@Qualifier("caffeineProperties") Map<String,String> properties){

        CaffeineCacheManager manager = new CaffeineCacheManager();
        Map.Entry<String,String> entry;
        Iterator<Map.Entry<String,String>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()){
            entry = iterator.next();
            manager.registerCustomCache(entry.getKey(), Caffeine.from(entry.getValue()).build());
        }
        return manager;
    }
}