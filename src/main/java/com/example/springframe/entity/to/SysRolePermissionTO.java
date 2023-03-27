package com.example.springframe.entity.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.springframe.entity.SearchPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色权限关系(SysRolePermission)实体TO类
 *
 * @author makejava
 * @since 2023-03-27 10:21:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysRolePermissionTO对象", description = "角色权限关系TO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermissionTO extends SearchPage implements Serializable {
    private static final long serialVersionUID = -37228177444929377L;
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
