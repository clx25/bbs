package com.bbs.servie.impl;

import com.bbs.entity.Status;
import com.bbs.entity.database.ReplyDO;
import com.bbs.entity.database.ThreadDO;
import com.bbs.entity.database.UserDO;
import com.bbs.mapper.ReplyMapper;
import com.bbs.mapper.ThreadMapper;
import com.bbs.mapper.UserMapper;
import com.bbs.servie.ReplyMeService;
import com.bbs.servie.UserService;
import com.bbs.util.ResultUtil;
import com.bbs.util.StatusEnum;
import com.bbs.entity.Result;
import com.bbs.entity.vo.ReplyMeVO;
import com.bbs.exception.customexception.NotFoundException;
import com.bbs.mapper.ReplyMeMapper;
import com.bbs.util.EntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReplyMeServiceImpl implements ReplyMeService {


    private final UserService userServiceImpl;

    private final ReplyMapper replyMapper;
    private final ThreadMapper threadMapper;
    private final UserMapper userMapper;
    private final ReplyMeMapper replyMeMapper;

    private static final String THREAD = "帖子";
    private static final String POST = "回帖";


    @Override
    public Result<List<ReplyMeVO>> listReplyMe(String status) {
        //设置登录用户信息
        int userId = userServiceImpl.getUserId();
        List<ReplyMeVO> replyMeVOList=null;
        if ("unread".equals(status)) {
            //获取未读的消息
            replyMeVOList = listReplyMe(userId, false);

        } else if("history".equals(status)){
            replyMeVOList = listReplyMe(userId, true);
        }

        return ResultUtil.success(replyMeVOList);
    }

    /**
     * 获取用户回复信息
     * 情况1.这个消息回复的是该用户发送的帖子，那么无论是回帖还是楼中楼，都显示为对【帖子】发表了回复
     * 情况2.这个消息回复的不是该用户发送的帖子，就显示为对【回帖】发表了回复
     *
     * @param userId 用户id
     * @param read   是否已读
     * @return 回复信息集合，可能为null
     */
    private List<ReplyMeVO> listReplyMe(int userId, boolean read) {
        List<ReplyMeVO> replySet = new ArrayList<>();


        List<ReplyDO> replyDOList = replyMeMapper.listReplyMeByUserId(userId, read);
        if (replyDOList.size() == 0) {
            return null;
        }
        //获取回帖消息
        List<ReplyDO> postList = replyDOList.stream().filter(e -> e.getPostId() == 0).collect(Collectors.toList());
        if (postList.size() != 0) {
            Set<Integer> threadIds = postList.stream().map(ReplyDO::getThreadId).collect(Collectors.toSet());
            Map<Integer, ThreadDO> threadDOMap = threadMapper.listThreadInThreadIds(threadIds);
            setPostInfo(replySet, threadDOMap, postList);
        }

        //获取楼中楼消息
        List<ReplyDO> lzlList = replyDOList.stream().filter(e -> e.getPostId() != 0).collect(Collectors.toList());
        if (lzlList.size() != 0) {
            Set<Integer> postIds = lzlList.stream().map(ReplyDO::getPostId).collect(Collectors.toSet());
            Map<Integer, ReplyDO> replyDOMap = replyMapper.listReplyInIds(postIds);
            setLzlInfo(replySet, replyDOMap, lzlList);
        }

        setUserInfoToReplyMe(replySet);
        return replySet;
    }


    @Override
    public Status deleteReplyMe(int replyId) throws NotFoundException {
        int userId = userServiceImpl.getUserId();
        int i = replyMeMapper.deleteReplyMeIfExists(userId, replyId);
        if (i == 0) {
            throw new NotFoundException("该回复不在回复列表");
        }
        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    /**
     * 给回复消息中添加用户信息
     */
    private void setUserInfoToReplyMe(List<ReplyMeVO> replySet) {
        Set<Integer> userIds = replySet.stream().map(ReplyMeVO::getUserId).collect(Collectors.toSet());
        if (userIds.size() > 0) {
            Map<Integer, UserDO> userMap = userMapper.listUserByIds(userIds);
            for (ReplyMeVO reply : replySet) {
                UserDO user = userMap.get(reply.getUserId());
                if (user != null) {
                    EntityUtil.CopyToEntity(user, reply);
                }
            }
        }
    }


    /**
     * 设置回帖信息到ReplyMeAO
     *
     * @param replySet    返回到页面的信息集合
     * @param threadDOMap 回帖对应的帖子信息
     * @param postList    回帖信息
     */
    private void setPostInfo(List<ReplyMeVO> replySet,
                             Map<Integer, ThreadDO> threadDOMap,
                             List<ReplyDO> postList) {

        if (postList.size() == 0 || threadDOMap.size() == 0) {
            return;
        }
        for (ReplyDO post : postList) {
            ThreadDO threadDO = threadDOMap.get(post.getThreadId());
            if (threadDO != null) {
                populateReplyContent(replySet, post, THREAD, threadDO.getTitle(), threadDO.getId());
            }
        }
    }

    /**
     * 设置楼中楼的信息到页面实体类
     *
     * @param replySet   返回到页面的信息集合
     * @param replyDOMap 楼中楼对应的回帖信息
     * @param lzlList    楼中楼信息
     */
    private void setLzlInfo(List<ReplyMeVO> replySet,
                            Map<Integer, ReplyDO> replyDOMap,
                            List<ReplyDO> lzlList) {
        if (lzlList.size() == 0 || replyDOMap.size() == 0) {
            return;
        }
        for (ReplyDO lzl : lzlList) {
            ReplyDO replyDO = replyDOMap.get(lzl.getPostId());
            if (replyDO != null) {
                populateReplyContent(replySet, lzl, POST, lzl.getContent(), lzl.getId());
            }
        }


    }

    /**
     * 填充返回页面的消息实体类
     *
     * @param replySet 返回到页面的信息集合
     * @param replyDO  回复信息
     * @param type     回复目标类型
     * @param target   目标内容
     * @param targetId 目标id
     */
    private void populateReplyContent(List<ReplyMeVO> replySet, ReplyDO replyDO, String type, String target, int targetId) {

        ReplyMeVO reply = new ReplyMeVO();
        reply.setType(type);
        reply.setThreadId(replyDO.getThreadId());
        reply.setTargetId(targetId);
        reply.setUserId(replyDO.getUserId());
        reply.setReplyTarget(target);
        reply.setReplyId(replyDO.getId());
        reply.setReplyTime(replyDO.getGmtCreate());
        reply.setContent(replyDO.getContent());
        replySet.add(reply);
    }



    @Override
    public Status markRead(Integer replyId) throws NotFoundException {

        int userId = userServiceImpl.getUserId();

        if (replyId == null) {
            replyMeMapper.updateAllRead(userId);
        } else {
            int i = replyMeMapper.updateRead(userId, replyId);
            if (i == 0) {
                throw new NotFoundException("该回复不在回复列表");
            }
        }

        return ResultUtil.info(StatusEnum.SUCCESS);
    }
}
