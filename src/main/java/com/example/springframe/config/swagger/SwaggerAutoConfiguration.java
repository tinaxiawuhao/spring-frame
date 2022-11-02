package com.example.springframe.config.swagger;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
//注入配置类
@EnableConfigurationProperties({SwaggerProperties.class})
//根据配置文件决定是否自动配置
@ConditionalOnProperty(prefix = "swagger", name = "enabled", havingValue = "true")
@EnableSwagger2WebMvc
public class SwaggerAutoConfiguration {
    private static final String VERSION = "1.0.0";
    private final SwaggerProperties swaggerProperties;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalRequestParameters())
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("摘要", "", ""))
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(VERSION)
                .build();
    }

    private List<Parameter> globalRequestParameters() {
        ParameterBuilder parameterBuilder = new ParameterBuilder()
                //每次请求加载header
                .parameterType("header")
                //头标签
                .name("Authorization")
                .description("登录token")
                .modelRef(new ModelRef("string"))
                .required(false)
                ;
        return Collections.singletonList(parameterBuilder.build());
    }
}
