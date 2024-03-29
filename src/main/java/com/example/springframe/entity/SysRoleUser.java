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
 * 用户角色关系(SysRoleUser)实体类
 *
 * @author makejava
 * @since 2023-03-27 10:21:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_user")
@ApiModel(value = "SysRoleUser对象", description = "用户角色关系")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleUser extends Model<SysRoleUser> implements Serializable {
    private static final long serialVersionUID = -48260558375096686L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
}
