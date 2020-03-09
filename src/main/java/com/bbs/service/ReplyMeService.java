package com.bbs.service;

import com.bbs.entity.Status;
import com.bbs.entity.Result;
import com.bbs.entity.vo.ReplyMeVO;
import com.bbs.exception.custom.NotFoundException;

import java.util.List;

public interface ReplyMeService {


    /**
     * 根据状态获取回复消息
     * @param status 消息状态，可用的值unread(未读)，history(已读的历史消息)
     * @return 包含回复消息的集合
     */
    Result<List<ReplyMeVO>> listReplyMe(String status);

    /**
     * 在回复消息页面删除回复
     *
     * @param replyId 回复id
     * @return 操作状态消息
     * @throws NotFoundException 该回复不在这个登录用户的回复列表中（包括该回复不存在）
     */
    Status deleteReplyMe(int replyId) throws NotFoundException;


    /**
     * 标记消息为已读
     * @param replyId 回复id
     * @return  操作状态消息
     * @throws NotFoundException 没找到该回复
     */
    Status markRead(Integer replyId) throws NotFoundException;

}
