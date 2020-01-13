package com.own.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserVO {
    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("消息数量")
    private long msgCount;
}
