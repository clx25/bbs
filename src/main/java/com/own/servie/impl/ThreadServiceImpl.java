package com.own.servie.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.AddThreadDTO;
import com.own.entity.dto.ReplyDTO;
import com.own.entity.database.ThreadDO;
import com.own.entity.database.UserDO;
import com.own.entity.vo.ThreadVO;
import com.own.exception.customexception.NotFoundException;
import com.own.mapper.ReplyMapper;
import com.own.mapper.ThreadMapper;
import com.own.mapper.UserMapper;
import com.own.servie.ThreadService;
import com.own.servie.UserService;
import com.own.util.EntityUtil;
import com.own.util.ResultUtil;
import com.own.util.StatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ThreadServiceImpl implements ThreadService {


    @Resource
    private ThreadMapper threadMapper;

    @Resource
    private UserMapper userMapper;
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private UserService userService;


    @Override
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


    @Override
    public Result<ThreadVO> getThread(int threadId) throws NotFoundException {

        ThreadDO threadDO = threadMapper.getThreadByThreadId(threadId);
        if (threadDO == null) {
            throw new NotFoundException("帖子不存在");
        }
        ThreadVO threadVO = new ThreadVO();
        EntityUtil.CopyToEntity(threadDO, threadVO);
        return ResultUtil.success(threadVO);
    }

    /**
     * 填充List<ThreadVO>
     *
     * @param threadList   帖子列表
     * @param threadVOList 返回页面的帖子信息
     */
    private void populateThreadVOList(List<ThreadDO> threadList,
                                      List<ThreadVO> threadVOList) {
        //获取发帖用户信息
        Set<Integer> userIds = threadList.stream().map(ThreadDO::getUserId).collect(Collectors.toSet());
        Map<Integer, UserDO> userMap = userMapper.listUserByIds(userIds);


        //获取帖子回复数
        Set<Integer> threadIds = threadList.stream().map(ThreadDO::getId).collect(Collectors.toSet());
        Map<Integer, ThreadDO> replyCountMap = replyMapper.countReplyInThreadIds(threadIds);


        for (ThreadDO threadDO : threadList) {
            UserDO userDO = userMap.get(threadDO.getUserId());
            ThreadVO threadVO = new ThreadVO();
            if (userDO != null) {
                EntityUtil.CopyToEntity(userDO, threadVO);
                EntityUtil.CopyToEntity(threadDO, threadVO);
            }
            ThreadDO replyCount = replyCountMap.get(threadDO.getId());
            if (replyCount != null) {
                //第一个回复不统计在内
                threadVO.setReplyCount(replyCount.getReplyCount() - 1);
            }

            threadVOList.add(threadVO);
        }
    }


    @Transactional
    @Override
    public Status postThread(AddThreadDTO dto) {

        int userId = userService.getUserId();
        dto.setUserId(userId);

        threadMapper.addThread(dto);

        ReplyDTO replyDTO = new ReplyDTO();
        EntityUtil.CopyToEntity(dto, replyDTO);
        replyDTO.setFirst(true);
        replyMapper.insertReply(replyDTO);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }


}
