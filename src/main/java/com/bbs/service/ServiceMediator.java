package com.bbs.service;

import com.bbs.entity.database.BoardDO;
import com.bbs.entity.database.ReplyDO;
import com.bbs.entity.database.ThreadDO;
import com.bbs.entity.database.UserDO;
import com.bbs.entity.dto.ReplyDTO;
import com.bbs.exception.custom.ArgsNotValidException;
import com.bbs.exception.custom.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service之间通过这个类交互数据
 */
@Service
public class ServiceMediator {

    @Resource
    private UserService userService;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private BoardService boardService;
    @Resource
    private ReplyMeService replyMeService;
    @Resource
    private ReplyService replyService;
    @Resource
    private ThreadService threadService;


    public int getUserId() {
        return userService.getUserId();
    }

    public List<ThreadDO> listMyThread(int userId) {
        return threadService.listMyThread(userId);
    }

    public List<BoardDO> listBoardInBoardIds(Set<Integer> boardIds) {
        return boardService.listBoardInBoardIds(boardIds);
    }

    public int countReplyMe(int userId) {
        return replyMeService.countReplyMe(userId);
    }

    public void sendCaptchaAndSave(String email) {
        captchaService.sendCaptchaAndSave(email);
    }

    public void checkCaptcha(String email, String vCaptcha) throws ArgsNotValidException {
        captchaService.checkCaptcha(email, vCaptcha);
    }

    public List<UserDO> listUserByIds(Set<Integer> userIds) {
        return userService.listUserByIds(userIds);
    }

    public void insertReply(ReplyDTO dto) {
        replyService.insertReply(dto);
    }

    public void addReplyMe(int userId, int replyId) {
        replyMeService.addReplyMe(userId, replyId);
    }

    public Map<Integer, ThreadDO> countReplyInThreadIds(Set<Integer> threadIds) {
        return replyService.countReplyInThreadIds(threadIds);
    }

    public ThreadDO getThreadByThreadId(int threadId) throws NotFoundException {
        return threadService.getThreadByThreadId(threadId);
    }

    public void deleteThreadByThreadId(int threadId) {
        threadService.deleteThreadByThreadId(threadId);
    }

    public Map<Integer, ReplyDO> listReplyInIds(Set<Integer> replyIds) {
        return replyService.listReplyInIds(replyIds);
    }

    public Map<Integer, ThreadDO> listThreadInThreadIds(Set<Integer> threadIds) {
        return threadService.listThreadInThreadIds(threadIds);
    }

    public ReplyDO getReplyById(int replyId) throws NotFoundException {
        return replyService.getReplyById(replyId);
    }
}
