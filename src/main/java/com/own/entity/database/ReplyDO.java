package com.own.entity.database;

import com.own.annotation.FieldAlias;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * reply表
 */

@Data
public class ReplyDO {
    //回帖id
    @FieldAlias("replyId")
    private int id;
    //回复用户id
    private int userId;
    //回复内容
    private String content;
    //回复时间
    @FieldAlias("replyTime")
    private Date gmtCreate;
    //回复的帖子id
    private int threadId;
    //回帖的回复id
    private int postId;
    //是否帖子的第一条内容
    private boolean first;
    //楼中楼数量
    private int lzlCount;
}
