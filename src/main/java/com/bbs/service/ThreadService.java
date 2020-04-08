package com.bbs.service;

import com.bbs.entity.Result;
import com.bbs.entity.Status;
import com.bbs.entity.database.ThreadDO;
import com.bbs.entity.database.UserDO;
import com.bbs.entity.dto.AddThreadDTO;
import com.bbs.entity.dto.ReplyDTO;
import com.bbs.entity.vo.ThreadVO;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.mapper.ThreadMapper;
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
public class ThreadService {


    @Resource
    private ThreadMapper threadMapper;

    @Resource
    private ServiceMediator serviceMediator;

    /**
     * 获取帖子列表
     *
     * @param boardId 版块id
     * @param pageNum 第几页
     * @return 指定页的帖子列表
     */
    public Result<PageInfo<ThreadVO>> listThread(int boardId, int pageNum) {


        //分页获取版块下的帖子
        PageHelper.startPage(pageNum, 10);
        List<ThreadDO> threadList = threadMapper.listThreadByBoardId(boardId);
        if (threadList.size() == 0) {
            return ResultUtil.success(null);
        }


        //构建List<ThreadVO>信息
        List<ThreadVO> threadVOList = new ArrayList<>();
        populateThreadVOList(threadList, threadVOList);


        PageInfo<ThreadVO> pageInfo = new PageInfo<>((Page) threadList);
        pageInfo.setList(threadVOList);


        return ResultUtil.success(pageInfo);
    }


    /**
     * 获取帖子信息
     *
     * @param threadId 帖子id
     * @return 帖子信息
     * @throws NotFoundException 没找打帖子
     */
    public Result<ThreadVO> getThread(int threadId) throws NotFoundException {

        ThreadDO threadDO = getThreadByThreadId(threadId);

        ThreadVO threadVO = new ThreadVO();
        EntityUtil.CopyToEntity(threadDO, threadVO);
        return ResultUtil.success(threadVO);
    }

    /**
     * -------------------
     * 填充List<ThreadVO>
     *
     * @param threadList   帖子列表
     * @param threadVOList 返回页面的帖子信息
     */
    private void populateThreadVOList(List<ThreadDO> threadList,
                                      List<ThreadVO> threadVOList) {
        //获取发帖用户信息
        Set<Integer> userIds = threadList.stream().map(ThreadDO::getUserId).collect(Collectors.toSet());
        List<UserDO> userDOList = serviceMediator.listUserByIds(userIds);

        //获取帖子回复数
        Set<Integer> threadIds = threadList.stream().map(ThreadDO::getId).collect(Collectors.toSet());
        Map<Integer, ThreadDO> replyCountMap = serviceMediator.countReplyInThreadIds(threadIds);


        for (ThreadDO threadDO : threadList) {
            for (UserDO userDO : userDOList) {
                if (threadDO.getUserId() == userDO.getId()) {
                    ThreadVO threadVO = new ThreadVO();
                    EntityUtil.CopyToEntity(userDO, threadVO);
                    EntityUtil.CopyToEntity(threadDO, threadVO);
                    ThreadDO replyCount = replyCountMap.get(threadDO.getId());
                    if (replyCount != null) {
                        //第一个回复不统计在内
                        threadVO.setReplyCount(replyCount.getReplyCount() - 1);
                    }

                    threadVOList.add(threadVO);
                }
            }

        }
    }

    /**
     * 发布帖子
     *
     * @param dto 帖子信息
     * @return 操作状态
     */
    @Transactional
    public Status postThread(AddThreadDTO dto) {

        int userId = serviceMediator.getUserId();

        dto.setUserId(userId);

        threadMapper.addThread(dto);

        ReplyDTO replyDTO = new ReplyDTO();
        EntityUtil.CopyToEntity(dto, replyDTO);
        replyDTO.setFirst(true);
        serviceMediator.insertReply(replyDTO);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 获取指定用户发布的帖子集合
     *
     * @param userId 用户id
     * @return 指定用户发布的帖子集合
     */
    public List<ThreadDO> listMyThread(int userId) {
        return threadMapper.listMyThread(userId);
    }

    /**
     * 获取帖子
     *
     * @param threadId 帖子id
     * @return 帖子信息
     */
    public ThreadDO getThreadByThreadId(int threadId) throws NotFoundException {
        ThreadDO threadDO = threadMapper.getThreadByThreadId(threadId);
        if (threadDO == null) {
            throw new NotFoundException("版块不存在");
        }
        return threadDO;
    }

    /**
     * 删除帖子
     *
     * @param threadId 帖子id
     */
    public void deleteThreadByThreadId(int threadId) {
        threadMapper.deleteThreadByThreadId(threadId);
    }

    /**
     * 获取帖子集合
     *
     * @param threadIds 帖子id集合
     * @return 帖子集合
     */
    public Map<Integer, ThreadDO> listThreadInThreadIds(Set<Integer> threadIds) {
        return threadMapper.listThreadInThreadIds(threadIds);
    }
}
