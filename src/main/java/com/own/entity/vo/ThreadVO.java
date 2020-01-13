package com.own.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("帖子信息")
public class ThreadVO {
    @ApiModelProperty("帖子id")
    private int threadId;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("回复数量")
    private int replyCount;

    @ApiModelProperty("发帖时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

    @ApiModelProperty("发帖用户id")
    private int userId;

    @ApiModelProperty("帖子所属版块id")
    private int boardId;

    @ApiModelProperty("发帖用户名")
    private String username;



}
