package com.bbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class AccountVO {
    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("电话号码")
    private String tel;

    private long integral;

    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signupDate;

    @ApiModelProperty(value = "性别", dataType = "String")
    private int sex;

    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty("是否公开邮箱")
    private boolean emailPublic;

    @ApiModelProperty("是否公开电话号码")
    private boolean telPublic;


    public String getSex() {
        switch (this.sex) {
            case 1:
                return "男";
            case 2:
                return "女";
            case 3:
                return "保密";
        }
        return "";
    }
}
