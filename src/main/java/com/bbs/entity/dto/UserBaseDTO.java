package com.bbs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
class UserBaseDTO {
    @NotEmpty(message = "邮箱格式错误")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @Pattern(regexp = ".{6,20}", message = "密码为6到20个字符")
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;
}
