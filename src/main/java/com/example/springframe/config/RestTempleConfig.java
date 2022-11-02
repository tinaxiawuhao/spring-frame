package com.example.springframe.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTempleConfig {

    @Bean
    public RestTemplate restTemplate() {

        //生成一个设置了连接超时时间、请求超时时间
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(30000).build();

        // 设置异常重试
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create()
                        .setDefaultRequestConfig(config)
                        .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false)).build());
        // 日志拦截
//        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateConsumerLogger()));

        return new RestTemplate(requestFactory);
    }
}
