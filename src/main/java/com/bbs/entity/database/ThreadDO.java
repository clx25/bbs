package com.bbs.entity.database;

import com.bbs.annotation.FieldAlias;
import lombok.Data;

import java.util.Date;

/**
 * thread表
 */
@Data
public class ThreadDO {
    //帖子id
    @FieldAlias("threadId")
    private int id;
    //标题
    private String title;
    //内容
    private String content;
    //回复数
    private int replyCount;
    //版块id
    private int boardId;
    //用户id
    private int userId;
    //发布时间
    @FieldAlias("postTime")
    private Date gmtCreate;
    //最后一次更新时间
    private Date gmtModified;
}
