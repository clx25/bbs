package com.bbs.service;

import com.bbs.entity.Result;
import com.bbs.entity.Status;
import com.bbs.entity.database.ReplyDO;
import com.bbs.entity.database.ThreadDO;
import com.bbs.entity.database.UserDO;
import com.bbs.entity.dto.ReplyDTO;
import com.bbs.entity.vo.ReplyVO;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.mapper.ReplyMapper;
import com.bbs.util.EntityUtil;
import com.bbs.util.ResultUtil;
import com.bbs.util.StatusEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReplyService {


    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private ServiceMediator serviceMediator;

    /**
     * 分页获取回帖
     *
     * @param threadId 帖子id
     * @param pageNum  获取第几页回帖
     * @return 分页后的回帖集合
     */
    public Result<PageInfo<ReplyVO>> getPost(Integer threadId, Integer pageNum) {


        //分页获取回帖信息
        Page<ReplyVO> page = PageHelper.startPage(pageNum, 10);
        List<ReplyDO> postDOList = replyMapper.listPostByThreadId(threadId);
        if (postDOList.size() == 0) {
            return ResultUtil.success(null);
        }
        List<ReplyVO> replyVOList = setInfoToReplyVO(postDOList);


        //获取每个回帖的楼中楼数量
        Set<Integer> postIds = postDOList.stream().map(ReplyDO::getId).collect(Collectors.toSet());
        Map<Integer, ReplyDO> lzlCountMap = replyMapper.countLzlInPostIds(postIds);

        for (ReplyVO post : replyVOList) {
            ReplyDO lzlCount = lzlCountMap.get(post.getReplyId());
            if (lzlCount != null) {
                post.setReplyCount(lzlCount.getLzlCount());
            }
        }

        PageInfo<ReplyVO> pageInfo = new PageInfo<>(page);
        pageInfo.setList(replyVOList);

        return ResultUtil.success(pageInfo);
    }

    /**
     * 获取分页后的楼中楼集合
     *
     * @param postId  回帖id
     * @param pageNum 获取第几页楼中楼
     * @return 分页后的楼中楼集合
     * @throws NotFoundException 找不到该回帖
     */
    public Result<PageInfo<ReplyVO>> getLzl(int postId, int pageNum) throws NotFoundException {

        boolean exists = replyMapper.isReplyExists(postId);
        if (!exists) {
            throw new NotFoundException("回复不存在");
        }

        //如果结果为空，那么可能没有楼中楼，也可能这个回帖不存在，所以上面需要判断
        Page<ReplyVO> page = PageHelper.startPage(pageNum, 10);
        List<ReplyDO> lzlList = replyMapper.listLzlByPostId(postId);
        if (lzlList.size() == 0) {
            return ResultUtil.success(null);
        }

        List<ReplyVO> replyVOList = setInfoToReplyVO(lzlList);


        PageInfo<ReplyVO> pageInfo = new PageInfo<>(page);
        pageInfo.setList(replyVOList);

        return ResultUtil.success(pageInfo);
    }

    /**
     * 给回复实体类中添加信息
     *
     * @param replyDOList 回复实体类集合
     * @return 添加完信息的回复实体类集合
     */
    private List<ReplyVO> setInfoToReplyVO(List<ReplyDO> replyDOList) {
        //获取用户信息
        Set<Integer> userIds = replyDOList.stream().map(ReplyDO::getUserId).collect(Collectors.toSet());
        List<UserDO> userDOList = serviceMediator.listUserByIds(userIds);


        List<ReplyVO> replyVOList = new ArrayList<>();

        for (ReplyDO replyDO : replyDOList) {
            for (UserDO userDO : userDOList) {
                if (replyDO.getUserId() == userDO.getId()) {
                    ReplyVO replyVO = new ReplyVO();
                    EntityUtil.CopyToEntity(replyDO, replyVO);
                    EntityUtil.CopyToEntity(userDO, replyVO);
                    replyVOList.add(replyVO);
                }
            }
        }
        return replyVOList;
    }

    /**
     * 添加楼中楼
     *
     * @param postId 回帖id
     * @param dto    楼中楼信息
     * @return 操作状态
     * @throws NotFoundException 没有该回帖
     */
    public Status addLzl(int postId, ReplyDTO dto) throws NotFoundException {

        //获取回帖信息
        ReplyDO post = getReplyById(postId);
        dto.setPostId(postId);
        ThreadDO thread = serviceMediator.getThreadByThreadId(post.getThreadId());


        //添加回复
        addReply(dto, thread);

        //如果不是给自己的回帖添加楼中楼，且回帖用户不是发帖用户，那么这个回帖的用户能收到该楼中楼的消息
        if (post.getUserId() != dto.getUserId() && post.getUserId() != thread.getUserId()) {
            serviceMediator.addReplyMe(post.getUserId(), dto.getReplyId());
        }


        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 添加回帖
     *
     * @param threadId 帖子id
     * @param dto      回帖信息
     * @return 操作状态
     * @throws NotFoundException 找不到该版块
     */
    public Status addPost(int threadId, ReplyDTO dto) throws NotFoundException {
        ThreadDO thread = serviceMediator.getThreadByThreadId(threadId);

        //添加回复
        addReply(dto, thread);


        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 添加回复
     *
     * @param dto    回复数据
     * @param thread 该回复对应的帖子
     */
    private void addReply(ReplyDTO dto, ThreadDO thread) {
        //获取帖子信息
        dto.setThreadId(thread.getId());

        //添加用户信息
        int userId = serviceMediator.getUserId();
        dto.setUserId(userId);

        //插入数据
        replyMapper.insertReply(dto);

        //如果不是给自己的帖子回帖，那么楼主能接收到该回帖消息
        if (thread.getUserId() != userId) {
            serviceMediator.addReplyMe(thread.getUserId(), dto.getReplyId());
        }
    }

    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @return 操作状态
     */
    @Transactional
    public Status deleteReply(int replyId) throws NotFoundException {


        ReplyDO reply = getReplyById(replyId);

        //如果是该贴的第一条内容，则删除该贴的所有内容
        if (reply.isFirst()) {
            serviceMediator.deleteThreadByThreadId(reply.getThreadId());
            replyMapper.deleteReplyByThreadId(reply.getThreadId());
            return ResultUtil.info(StatusEnum.SUCCESS);

        }
        //如果是回帖要把该回帖下的楼中楼状态也改为删除
        if (reply.getPostId() == 0) {
            replyMapper.deleteLzlByPostId(replyId);
        }
        replyMapper.deleteReply(replyId);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 添加回复
     *
     * @param dto 回复信息
     */
    public void insertReply(ReplyDTO dto) {
        replyMapper.insertReply(dto);
    }

    /**
     * 获取指定帖子集合的回复数量
     *
     * @param threadIds 帖子id集合
     * @return 回复集合
     */
    public Map<Integer, ThreadDO> countReplyInThreadIds(Set<Integer> threadIds) {
        return replyMapper.countReplyInThreadIds(threadIds);
    }

    /**
     * 获取回复集合
     *
     * @param replyIds 回复id集合
     * @return 回复集合
     */
    public Map<Integer, ReplyDO> listReplyInIds(Set<Integer> replyIds) {
        return replyMapper.listReplyInIds(replyIds);
    }

    /**
     * 获取回复信息
     *
     * @param replyId 回复id
     * @return 回复信息
     * @throws NotFoundException 回复不存在
     */
    public ReplyDO getReplyById(int replyId) throws NotFoundException {
        ReplyDO replyDO = replyMapper.getReplyById(replyId);
        if (replyDO == null) {
            throw new NotFoundException("回复不存在");
        }
        return replyDO;
    }

}