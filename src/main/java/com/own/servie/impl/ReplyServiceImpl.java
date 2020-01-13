package com.own.servie.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.ReplyDTO;
import com.own.entity.database.ReplyDO;
import com.own.entity.database.ThreadDO;
import com.own.entity.database.UserDO;
import com.own.entity.vo.ReplyVO;
import com.own.exception.customexception.NotFoundException;
import com.own.mapper.ReplyMapper;
import com.own.mapper.ReplyMeMapper;
import com.own.mapper.ThreadMapper;
import com.own.mapper.UserMapper;
import com.own.servie.ReplyService;
import com.own.servie.UserService;
import com.own.util.EntityUtil;
import com.own.util.ResultUtil;
import com.own.util.StatusEnum;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ReplyServiceImpl implements ReplyService {


    @Resource
    private ReplyMapper replyMapper;
    @Resource
    private ReplyMeMapper replyMeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadMapper threadMapper;

    @Resource
    private UserService userService;


    @Override
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


    @Override
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

    private List<ReplyVO> setInfoToReplyVO(List<ReplyDO> replyDOList) {
        //获取用户信息
        Set<Integer> userIds = replyDOList.stream().map(ReplyDO::getUserId).collect(Collectors.toSet());
        Map<Integer, UserDO> userMap = userMapper.listUserByIds(userIds);

        List<ReplyVO> replyVOList = new ArrayList<>();

        for (ReplyDO replyDO : replyDOList) {
            UserDO user = userMap.get(replyDO.getUserId());
            if (user != null) {
                ReplyVO replyVO = new ReplyVO();
                EntityUtil.CopyToEntity(replyDO, replyVO);
                EntityUtil.CopyToEntity(user, replyVO);
                replyVOList.add(replyVO);
            }
        }
        return replyVOList;
    }


    @Override
    public Status addLzl(int postId, ReplyDTO dto) throws NotFoundException {

        //获取回帖信息
        ReplyDO post = replyMapper.getReplyById(postId);
        if (post == null) {
            throw new NotFoundException("回帖不存在");
        }
        dto.setPostId(postId);
        ThreadDO thread = threadMapper.getThreadByThreadId(post.getThreadId());


        //添加回复
        addReply(dto, thread);

        //如果不是给自己的回帖添加楼中楼，且回帖用户不是发帖用户，那么这个回帖的用户能收到该楼中楼的消息
        if (post.getUserId() != dto.getUserId() && post.getUserId() != thread.getUserId()) {
            replyMeMapper.addReplyMe(post.getUserId(), dto.getReplyId());
        }


        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    @Override
    public Status addPost(int threadId, ReplyDTO dto) throws NotFoundException {
        ThreadDO thread = threadMapper.getThreadByThreadId(threadId);
        if (thread == null) {
            throw new NotFoundException("版块不存在");
        }

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
        int userId = userService.getUserId();
        dto.setUserId(userId);

        //插入数据
        replyMapper.insertReply(dto);

        //如果不是给自己的帖子回帖，那么楼主能接收到该回帖消息
        if (thread.getUserId() != userId) {
            replyMeMapper.addReplyMe(thread.getUserId(), dto.getReplyId());
        }
    }


    @Transactional
    @Override
    public Status deleteReply(int replyId) throws NotFoundException {


        ReplyDO reply = replyMapper.getReplyById(replyId);
        if (reply == null) {
            throw new NotFoundException("回复不存在");
        }

        if (!deletePermissionCheck(reply)) {
            throw new UnauthorizedException("没有删除权限");
        }
        //如果是该贴的第一条内容，则删除该贴的所有内容
        if (reply.isFirst()) {
            threadMapper.deleteThreadByThreadId(reply.getThreadId());
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
     * 校验该用户是否有删除这个回复的权限
     *
     * @param reply 需要被校验的回复
     * @return true为有删除权限，false为没有删除权限
     */
    private boolean deletePermissionCheck(ReplyDO reply) {
        int userId = userService.getUserId();
        //这个回复是该用户发的
        if (reply.getUserId() == userId) {
            return true;
        }
        //是该用户回帖的楼中楼
        if (reply.getPostId() != 0) {
            ReplyDO post = replyMapper.getReplyById(reply.getPostId());
            if (post.getUserId() == userId) {
                return true;
            }
        }
        //是该用户帖子中的回复
        ThreadDO thread = threadMapper.getThreadByThreadId(reply.getThreadId());
        return thread.getUserId() == userId;
    }
}