package com.bbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ReplyVO {

    @ApiModelProperty("回复id")
    private int replyId;

    @ApiModelProperty("回复内容")
    private String content;

    @ApiModelProperty("回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replyTime;

    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("回复内容")
    private int replyCount;

    @ApiModelProperty("是不是第一个条回复")
    private boolean first;


}
