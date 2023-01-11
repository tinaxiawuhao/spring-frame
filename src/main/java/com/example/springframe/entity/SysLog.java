package com.example.springframe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author
 * @date 2021-10-13 11:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysLog对象", description="系统日志")
@Builder
public class SysLog extends Model<SysLog> implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
