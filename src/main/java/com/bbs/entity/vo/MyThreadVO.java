package com.bbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
public class MyThreadVO  {
    @ApiModelProperty("帖子id")
    private int threadId;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("帖子所属版块id")
    private int boardId;

    @ApiModelProperty("帖子所属版块名")
    private String boardName;

    @ApiModelProperty("发帖时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date postTime;

}
