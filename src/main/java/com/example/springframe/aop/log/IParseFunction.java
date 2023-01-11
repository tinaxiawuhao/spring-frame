package com.example.springframe.aop.log;

public interface IParseFunction {

    default boolean executeBefore(){
        return false;
    }

    String functionName();

    String apply(Object... value);
}
