package com.example.springframe.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtPropertiesConfig {

    private String header;
    private String secret;
    private Long expiration;
    private String tokenHead;
    private List<String> pubUris;
    private List<String> staticSources;
}
