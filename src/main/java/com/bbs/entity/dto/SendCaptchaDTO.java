package com.bbs.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("发送验证码需要的数据")
public class SendCaptchaDTO {
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;

}
