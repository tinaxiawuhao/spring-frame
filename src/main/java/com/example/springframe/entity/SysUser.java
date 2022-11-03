package com.example.springframe.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户详情(SysUser)实体类
 *
 * @author makejava
 * @since 2022-11-03 11:03:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户详情")
@Builder
public class SysUser extends Model<SysUser> implements Serializable {
    private static final long serialVersionUID = 995005384156023164L;
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
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 最后一次更新密码时间
     */
    @ApiModelProperty(value = "最后一次更新密码时间")
    private Date lastPasswordResetDate;
    /**
     * 用户类型，0平台管理员，1租户管理员，2租户用户
     */
    @ApiModelProperty(value = "用户类型，0平台管理员，1租户管理员，2租户用户")
    private Integer userType;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String mobile;
    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮件地址")
    private String email;
    /**
     * 性别，0男，1女
     */
    @ApiModelProperty(value = "性别，0男，1女")
    private Integer sex;
    /**
     * 是否可用，0可用，1禁用
     */
    @ApiModelProperty(value = "是否可用，0可用，1禁用")
    private Integer state;
    /**
     * 是否已删除，0未删除，1已删除
     */
    @ApiModelProperty(value = "是否已删除，0未删除，1已删除")
    private Integer deleted;
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
}

