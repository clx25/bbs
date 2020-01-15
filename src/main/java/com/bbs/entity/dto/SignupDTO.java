package com.bbs.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("注册需要的数据")
public class SignupDTO extends UserBaseDTO {


    @NotEmpty(message = "用户名不能为空")
    @Size(min = 1,max = 20,message ="用户名必须在20个字符之内")
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @JsonIgnore
    private String salt;


    @ApiModelProperty(value = "验证码",required = true)
    @NotEmpty(message = "验证码不能为空")
    private String captcha;
}
