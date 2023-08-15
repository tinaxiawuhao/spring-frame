package com.example.springframe.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.springframe.utils.tree.TreeExtend;
import com.example.springframe.utils.tree.TreeField;
import com.example.springframe.utils.tree.TreeProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 权限详情(SysPermission)实体类
 *
 * @author makejava
 * @since 2023-03-27 10:21:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value = "SysPermission对象", description = "权限详情")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission extends TreeExtend implements Serializable {
    private static final long serialVersionUID = 142209922999056007L;
    /**
     * 主键
     */
    @TreeProperty(TreeField.ID)
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标识主键
     */
    @ApiModelProperty(value = "标识主键")
    private String code;
    /**
     * 菜单所属系统，0平台端，1租户端
     */
    @ApiModelProperty(value = "菜单所属系统，0平台端，1租户端")
    private Integer type;
    /**
     * 菜单名称
     */
    @TreeProperty(TreeField.NAME)
    @ApiModelProperty(value = "菜单名称")
    private String name;
    /**
     * 功能说明
     */
    @ApiModelProperty(value = "功能说明")
    private String description;
    /**
     * 父id
     */
    @TreeProperty(TreeField.PARENT_ID)
    @ApiModelProperty(value = "父id")
    private Integer pid;
    /**
     * url路径
     */
    @ApiModelProperty(value = "url路径")
    private String url;
    /**
     * 菜单类型;0菜单，1按钮
     */
    @ApiModelProperty(value = "菜单类型;0菜单，1按钮")
    private Integer menuType;
    /**
     * 是否可用，0可用，1禁用
     */
    @ApiModelProperty(value = "是否可用，0可用，1禁用")
    private Integer state;
    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String keystr;
    /**
     * 前端路由
     */
    @ApiModelProperty(value = "前端路由")
    private String routing;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * sort
     */
    @TreeProperty(TreeField.SORT)
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
