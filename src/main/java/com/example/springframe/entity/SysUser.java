package com.example.springframe.entity;

import java.util.Date;

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
 * 用户详情(SysUser)实体类
 *
 * @author makejava
 * @since 2023-02-09 10:29:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户详情")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends Model<SysUser> implements Serializable {
    private static final long serialVersionUID = 984975073483122506L;
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
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    @NotNull(message = "昵称不能为空")
    private String nickName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 最后一次更新密码时间
     */
    @ApiModelProperty(value = "最后一次更新密码时间", required = true)
    @NotNull(message = "最后一次更新密码时间不能为空")
    private Date lastPasswordResetDate;
    /**
     * 用户类型，0平台管理员，1租户管理员，2租户用户
     */
    @ApiModelProperty(value = "用户类型，0平台管理员，1租户管理员，2租户用户", required = true)
    @NotNull(message = "用户类型，0平台管理员，1租户管理员，2租户用户不能为空")
    private Integer userType;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码", required = true)
    @NotNull(message = "电话号码不能为空")
    private String mobile;
    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮件地址", required = true)
    @NotNull(message = "邮件地址不能为空")
    private String email;
    /**
     * 性别，0男，1女
     */
    @ApiModelProperty(value = "性别，0男，1女", required = true)
    @NotNull(message = "性别，0男，1女不能为空")
    private Integer sex;
    /**
     * 是否可用，0可用，1禁用
     */
    @ApiModelProperty(value = "是否可用，0可用，1禁用", required = true)
    @NotNull(message = "是否可用，0可用，1禁用不能为空")
    private Integer state;
    /**
     * 是否已删除，0未删除，1已删除
     */
    @ApiModelProperty(value = "是否已删除，0未删除，1已删除", required = true)
    @NotNull(message = "是否已删除，0未删除，1已删除不能为空")
    private Integer deleted;
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
