package com.bbs.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 展示聊天消息
 * 该实体类也用于保存聊天窗口发送的数据
 *
 */
@Data
public class ChatVO {
    //用户id
    private int userId;
    //用户名
    private String username;
    //头像文件名
    private String avatar;
    //聊天发送时间
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private Date time;
    //聊天内容
    private String chatMsg;
}
