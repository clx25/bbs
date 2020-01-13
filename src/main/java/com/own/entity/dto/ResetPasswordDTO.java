package com.own.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("重置密码需要的数据")
public class ResetPasswordDTO  extends UserBaseDTO{
    @ApiModelProperty(value = "验证码",required = true)
    @NotEmpty(message = "验证码不能为空")
    private String captcha;
}
