package com.example.springframe.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    @ApiModelProperty(value = "用户名",required = true)
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotNull(message = "密码不能为空")
    private String password;
}
