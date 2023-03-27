package com.example.springframe.entity.to;

import java.util.Date;

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
 * 权限详情(SysPermission)实体TO类
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysPermissionTO对象", description = "权限详情TO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPermissionTO extends SearchPage implements Serializable {
    private static final long serialVersionUID = -12732046493826219L;
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
     * 菜单所属系统，0平台端，1租户端
     */
    @ApiModelProperty(value = "菜单所属系统，0平台端，1租户端", required = true)
    @NotNull(message = "菜单所属系统，0平台端，1租户端不能为空")
    private Integer type;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", required = true)
    @NotNull(message = "菜单名称不能为空")
    private String name;
    /**
     * 功能说明
     */
    @ApiModelProperty(value = "功能说明", required = true)
    @NotNull(message = "功能说明不能为空")
    private String description;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id", required = true)
    @NotNull(message = "父id不能为空")
    private Integer pid;
    /**
     * url路径
     */
    @ApiModelProperty(value = "url路径", required = true)
    @NotNull(message = "url路径不能为空")
    private String url;
    /**
     * 菜单类型;0菜单，1按钮
     */
    @ApiModelProperty(value = "菜单类型;0菜单，1按钮", required = true)
    @NotNull(message = "菜单类型;0菜单，1按钮不能为空")
    private Integer menuType;
    /**
     * 是否可用，0可用，1禁用
     */
    @ApiModelProperty(value = "是否可用，0可用，1禁用", required = true)
    @NotNull(message = "是否可用，0可用，1禁用不能为空")
    private Integer state;
    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识", required = true)
    @NotNull(message = "权限标识不能为空")
    private String keystr;
    /**
     * 前端路由
     */
    @ApiModelProperty(value = "前端路由", required = true)
    @NotNull(message = "前端路由不能为空")
    private String routing;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", required = true)
    @NotNull(message = "创建者不能为空")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = true)
    @NotNull(message = "创建时间不能为空")
    private Date createTime;
    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者", required = true)
    @NotNull(message = "修改者不能为空")
    private String updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", required = true)
    @NotNull(message = "修改时间不能为空")
    private Date updateTime;
}
