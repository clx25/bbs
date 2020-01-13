package com.own.servie;

import com.github.pagehelper.PageInfo;
import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.ReplyDTO;
import com.own.entity.vo.ReplyVO;
import com.own.exception.customexception.NotFoundException;


public interface ReplyService {


    /**
     * 获取回帖
     *
     * @param threadId 帖子id
     * @param pageNum  第几页
     * @return 回帖实体类
     */
    Result<PageInfo<ReplyVO>> getPost(Integer threadId, Integer pageNum);


    /**
     * 添加楼中楼
     *
     * @param postId 回帖id
     * @param dto    楼中楼信息
     * @return 操作状态消息
     * @throws NotFoundException 回帖不存在
     */
    Status addLzl(int postId, ReplyDTO dto) throws NotFoundException;

    /**
     * 发表回帖
     *
     * @param threadId 帖子id
     * @param dto      回帖信息
     * @return 操作状态消息
     * @throws NotFoundException 找不到该版块
     */
    Status addPost(int threadId, ReplyDTO dto) throws NotFoundException;

    /**
     * 获取楼中楼信息
     *
     * @param postId  回帖id
     * @param pageNum 第几页
     * @return 返回页面的楼中楼信息和分页信息
     */
    Result<PageInfo<ReplyVO>> getLzl(int postId, int pageNum) throws NotFoundException;


    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @return 操作状态消息
     * @throws NotFoundException 找不到该回复
     */
    Status deleteReply(int replyId) throws NotFoundException;


}
