package com.bbs.mapper;

import com.bbs.entity.database.ReplyDO;
import com.bbs.entity.dto.ReplyDTO;
import com.bbs.entity.database.ThreadDO;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ReplyMapper {


    /**
     * 添加回复
     *
     * @param dto 添加回复需要的数据
     */
    void insertReply(ReplyDTO dto);


    /**
     * 回帖的楼中楼数量
     *
     * @param postIds 回帖id
     * @return key是回帖id, value是回复数据，只包含了楼中楼数量
     */
    @MapKey("postId")
    Map<Integer, ReplyDO> countLzlInPostIds(Set<Integer> postIds);


    /**
     * 根据帖子id获取回帖
     *
     * @param threadId 帖子id
     * @return 回帖数据集合
     */
    List<ReplyDO> listPostByThreadId(long threadId);


    /**
     * 根据回复id获取回复
     *
     * @param replyId 回复id
     * @return 回复数据
     */
    ReplyDO getReplyById(int replyId);

    /**
     * 根据回复id判断回复是否存在
     *
     * @param replyId 回帖id
     * @return true存在，false不存在
     */
    boolean isReplyExists(int replyId);

    /**
     * 根据回复id集合获取回复数据
     *
     * @param replyIds 回复id集合
     * @return key为回复id, value为回复数据
     */
    @MapKey("id")
    Map<Integer, ReplyDO> listReplyInIds(Set<Integer> replyIds);

    /**
     * 删除回复
     *
     * @param replyId 回复id
     */
    void deleteReply(int replyId);

    /**
     * 根据帖子id删除回复
     *
     * @param threadId 帖子id
     */
    void deleteReplyByThreadId(int threadId);

    /**
     * 根据回帖id获取楼中楼信息
     *
     * @param postId 回帖id
     * @return 楼中楼信息集合
     */
    List<ReplyDO> listLzlByPostId(int postId);


    /**
     * 根据回帖id删除楼中楼
     *
     * @param postId 回帖id
     */
    void deleteLzlByPostId(int postId);


    /**
     * 获取帖子的回复数量
     *
     * @param threadIds 帖子id集合
     * @return 帖子和回复数实体类
     */
    @MapKey("id")
    Map<Integer, ThreadDO> countReplyInThreadIds(Set<Integer> threadIds);


}
