package com.own.mapper;

import com.own.entity.dto.AddThreadDTO;
import com.own.entity.database.ThreadDO;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ThreadMapper {


    /**
     * 根据版块id获取版块下的所有帖子
     *
     * @param boardId 版块id
     * @return 帖子数据集合
     */
    List<ThreadDO> listThreadByBoardId(int boardId);


    /**
     * 根据帖子id获取帖子信息
     *
     * @param threadId 帖子id
     * @return 帖子信息
     */
    ThreadDO getThreadByThreadId(int threadId);


    /**
     * 添加帖子
     *
     * @param dto 帖子数据实体类
     */
    void addThread(AddThreadDTO dto);


    /**
     * 根据帖子id删除回复
     *
     * @param threadId 帖子id
     */
    void deleteThreadByThreadId(int threadId);


    /**
     * 根据帖子id集合获取帖子集合
     *
     * @param threadIds 帖子id集合
     * @return key为帖子id, value为帖子信息
     */
    @MapKey("id")
    Map<Integer, ThreadDO> listThreadInThreadIds(Set<Integer> threadIds);


    /**
     * 获取用户发布的帖子
     *
     * @param userId 用户id
     * @return 帖子信息集合
     */
    List<ThreadDO> listMyThread(int userId);
}
