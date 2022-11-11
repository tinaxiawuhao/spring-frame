package com.example.springframe.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class RigistUserVO {
    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码")
    private String messageCode;
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
}
