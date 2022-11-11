package com.example.springframe.utils;

import com.alibaba.fastjson.JSON;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.APIResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnWrite {
    public static void writeResp(HttpServletResponse response, APIResponse resp) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSON.toJSONString(resp));
    }
}
