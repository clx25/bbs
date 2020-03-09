package com.bbs.service;

import com.bbs.entity.Status;
import com.bbs.entity.dto.AddThreadDTO;
import com.bbs.entity.vo.ThreadVO;
import com.github.pagehelper.PageInfo;
import com.bbs.entity.Result;
import com.bbs.exception.custom.NotFoundException;

public interface ThreadService {


    /**
     * 根据版块id分页获取该版块下的帖子信息
     *
     * @param boardId 版块id
     * @param pageNum 第几页
     * @return 版块信息实体类
     */
    Result<PageInfo<ThreadVO>> listThread(int boardId, int pageNum);


    /**
     * 获取单条帖子信息
     *
     * @param threadId 帖子id
     * @exception NotFoundException 没有找到该帖子
     * @return 返回页面的帖子数据
     */
    Result<ThreadVO> getThread(int threadId) throws NotFoundException;


    /**
     * 添加帖子，帖子的内容作为第一条回帖
     *
     * @param dto 从页面接收的帖子信息实体类
     * @return 返回页面的消息
     */
    Status postThread(AddThreadDTO dto)  ;



}
