package com.example.springframe.aop.log;

import lombok.Data;

/**
 * @author
 * @date 2021-10-13 11:02
 */
@Data
public class SysLogExtBO {
    
    /**
     * 日志id
     */
    private Long logId;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回值
     */
    private String result;

    /**
     * 异常描述
     */
    private String exDetail;

}
