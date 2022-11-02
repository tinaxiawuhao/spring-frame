package com.example.springframe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPermission对象", description="权限详情")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标识主键")
    private String code;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "功能说明")
    private String description;

    @ApiModelProperty(value = "父id")
    private Integer pid;

    @ApiModelProperty(value = "url路径")
    private String url;

    @ApiModelProperty(value = "菜单类型;0菜单，1按钮")
    private Integer menuType;

    @ApiModelProperty(value = "是否可用，0可用，1禁用")
    private Integer state;

    @ApiModelProperty(value = "权限标识")
    private String keystr;

    @ApiModelProperty(value = "前端路由")
    private String routing;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
