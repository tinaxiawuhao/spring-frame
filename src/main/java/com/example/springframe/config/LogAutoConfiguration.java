package com.example.springframe.config;

import com.example.springframe.aop.log.IParseFunction;
import com.example.springframe.aop.log.ParseFunctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author
 * @date 2021-10-14 9:16
 */
@Configuration
public class LogAutoConfiguration {

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

}
