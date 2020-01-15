package com.bbs.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@ApiModel("修改账户信息需要的数据")
@Validated
public class UpdateAccountDTO {
    @JsonIgnore
    private int userId;

    @Pattern(regexp = ".{3,20}", message = "用户名为3到20个字符")
    @ApiModelProperty("用户名")
    private String username;

    @Past(message = "生日日期错误")
    @ApiModelProperty("生日日期")
    private Date birthday;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 3, message = "性别参数错误")
    @ApiModelProperty(value = "性别:男，女，保密", dataType = "String")
    private int sex;


    @Pattern(regexp = "^([1][3-9][0-9]{9})?$", message = "电话格式错误")
    @ApiModelProperty("电话号码")
    private String tel;


    @Pattern(regexp = "^(.{6,20})?$", message = "旧密码格式错误")
    @ApiModelProperty("旧密码")
    private String oldPassword;


    @Pattern(regexp = "^(.{6,20})?$", message = "新密码必须在6到20个字符之内")
    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("是否公开邮箱")
    private Boolean emailPublic;

    @ApiModelProperty("是否公开电话号码")
    private Boolean telPublic;


    public void setSex(String sex) {
        if (StringUtils.hasLength(sex)) {
            switch (sex) {
                case "男":
                    this.sex = 1;
                    break;
                case "女":
                    this.sex = 2;
                    break;
                case "保密":
                    this.sex = 3;
                    break;
                default:
                    this.sex = -1;
                    break;
            }
        }

    }
}
