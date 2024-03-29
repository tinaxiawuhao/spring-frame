package com.example.springframe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色详情(SysRole)实体类
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "角色详情")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRole extends Model<SysRole> implements Serializable {
    private static final long serialVersionUID = 167031774011543489L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标识主键
     */
    @ApiModelProperty(value = "标识主键")
    private String code;
    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    private String role;
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String roleDescription;
}
