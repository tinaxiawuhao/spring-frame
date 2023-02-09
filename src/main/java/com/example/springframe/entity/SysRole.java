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
 * @since 2023-02-09 10:29:21
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
    private static final long serialVersionUID = 543293279602848601L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标识主键
     */
    @ApiModelProperty(value = "标识主键", required = true)
    @NotNull(message = "标识主键不能为空")
    private String code;
    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名", required = true)
    @NotNull(message = "角色名不能为空")
    private String role;
    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述", required = true)
    @NotNull(message = "角色描述不能为空")
    private String roleDescription;
}
