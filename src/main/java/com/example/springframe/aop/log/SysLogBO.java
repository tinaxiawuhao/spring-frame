package com.example.springframe.aop.log;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author
 * @date 2021-10-13 11:00
 */
@Data
public class SysLogBO {

    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 日志类型 1:操作类型 2:异常类型
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;

    /**
     * 浏览器
     */
    private String ua;

    /**
     * 扩展日志信息
     */
    private SysLogExtBO logExt;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建用户id
     */
    private Integer createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 修改用户id
     */
    private Integer updateById;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
