package com.bbs.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * thread.html页面传入的添加回帖或添加楼中楼信息
 */
@Data
public class ReplyDTO {
    @NotEmpty(message = "内容不能为空")
    @Size(max = 1000, message = "内容不能大于1000字")
    @ApiModelProperty("回复内容")
    private String content;

    //帖子id
    @JsonIgnore
    private int replyId;
    //用户id
    @JsonIgnore
    private int userId;
    //回帖id
    @JsonIgnore
    private int postId;
    //帖子id
    @JsonIgnore
    private int threadId;
    //是否帖子的内容
    @JsonIgnore
    private boolean first;
}
