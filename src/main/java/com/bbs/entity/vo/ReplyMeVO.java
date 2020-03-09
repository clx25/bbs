package com.bbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data

public class ReplyMeVO {
    @ApiModelProperty("回复用户id")
    private int userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("回复的目标id")
    private int targetId;

    @ApiModelProperty("回复的目标内容")
    private String replyTarget;

    @ApiModelProperty("回复的目标类型")
    private String type;

    @ApiModelProperty("回复内容")
    private String content;

    @ApiModelProperty("回复id")
    private int replyId;

    @ApiModelProperty("回复时间")
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private Date replyTime;

    @ApiModelProperty("回复对应的帖子id")
    private int threadId;

}
