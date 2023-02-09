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
 * 角色权限关系(SysRolePermission)实体类
 *
 * @author makejava
 * @since 2023-02-09 10:29:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_permission")
@ApiModel(value = "SysRolePermission对象", description = "角色权限关系")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission extends Model<SysRolePermission> implements Serializable {
    private static final long serialVersionUID = -83552416497593385L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限id", required = true)
    @NotNull(message = "权限id不能为空")
    private Integer permissionId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
}
