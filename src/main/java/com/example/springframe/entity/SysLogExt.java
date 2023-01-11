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
 * @date 2021-10-13 11:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysLogExt对象", description="系统日志扩展字段")
@Builder
public class SysLogExt extends Model<SysLogExt> implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
