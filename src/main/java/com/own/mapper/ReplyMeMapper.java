package com.own.mapper;

import com.own.entity.database.ReplyDO;

import java.util.List;

public interface ReplyMeMapper {

    /**
     * 添加用户收到的消息
     * @param userId    收到消息的用户id
     * @param replyId   收到的回复id
     */
    void addReplyMe(int userId,int replyId);



    /**
     * 获取用户收到的消息数量
     * @param userId 用户id
     * @return  消息数量
     */
    int countReplyMe(int userId);

    /**
     * 如果这条回复存在于该用户的回复消息列表中，就删除
     * @param userId    用户id
     * @param replyId   回复id
     * @return  操作成功的条数
     */
    int deleteReplyMeIfExists(int userId,int replyId);

    /**
     * 标记该用户的指定回复为已读
     * @param userId    用户id
     * @param replyId   回复id
     */
    int updateRead(int userId,int replyId);

    /**
     * 标记该用户的所有回复消息为已读
     * @param userId    用户id
     */
    void updateAllRead(int userId);




    /**
     * 根据用户id获取回复
     * @param userId 用户id
     * @param read  是否已读，true查询已读，false查询未读
     * @return  回复集合
     */
    List<ReplyDO> listReplyMeByUserId(int userId,boolean read);
}
