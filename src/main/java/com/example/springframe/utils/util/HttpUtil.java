package com.example.springframe.utils.util;


import cn.hutool.json.JSONObject;
import com.example.springframe.exception.BusinessException;
import com.example.springframe.exception.basic.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class HttpUtil {

    @Resource
    private RestTemplate restTemplate;


    /**
     * 发送请求Get请求（添加后缀参数，添加请求头）
     *
     * @param url   请求接口
     * @param params 请求参数
     * @param headers 请求头
     */
    public Object sendGet(String url, Map<String, Object> params, HttpHeaders headers)throws BusinessException {
        try {
            StringBuilder stringBuffer = new StringBuilder(url);
            if (params != null) {
                Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                if (iterator.hasNext()) {
                    stringBuffer.append("?");
                    Map.Entry<String, Object> element;
                    while (iterator.hasNext()) {
                         element = iterator.next();
                        if (element.getValue() != null) {
                            stringBuffer.append(element).append("&");
                        }
                        url = stringBuffer.substring(0, stringBuffer.length() - 1);
                    }
                }
            }
            //设置请求头
            if(Objects.isNull(headers)){
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String>  resEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            return resEntity.getBody();
        } catch (Exception e) {
            if (e.toString().contains("timed out")) {
                throw new BusinessException(ResponseCode.CONNECTION_TIMEOUT);
            }
            log.error("接口调用失败",e);
            throw new BusinessException(ResponseCode.GETDATA_FAIL);
        }
    }

    /**
     * 发送请求Post请求
     *
     * @param url   请求接口
     * @param params 请求参数
     * @param headers 请求头
     */
    public JSONObject sendPost(String url, Map<String, Object> params, HttpHeaders headers) {
        try {
            //设置请求头
            if(Objects.isNull(headers)){
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            return restTemplate.postForObject(url, new HttpEntity<>(params, headers), JSONObject.class);
        } catch (Exception e) {
            if (e.toString().contains("timed out")) {
                throw new BusinessException(ResponseCode.CONNECTION_TIMEOUT);
            }
            throw new BusinessException(ResponseCode.GETDATA_FAIL);
        }
    }
}
