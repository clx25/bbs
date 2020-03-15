package com.bbs.service.impl;

import com.bbs.entity.Result;
import com.bbs.entity.Status;
import com.bbs.entity.database.BoardDO;
import com.bbs.entity.database.ThreadDO;
import com.bbs.entity.database.UserDO;
import com.bbs.entity.dto.*;
import com.bbs.entity.vo.AccountVO;
import com.bbs.entity.vo.MyThreadVO;
import com.bbs.entity.vo.UserVO;
import com.bbs.exception.custom.ArgsNotValidException;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.mapper.BoardMapper;
import com.bbs.mapper.ReplyMeMapper;
import com.bbs.mapper.ThreadMapper;
import com.bbs.mapper.UserMapper;
import com.bbs.service.CaptchaService;
import com.bbs.service.UserService;
import com.bbs.util.*;
import com.google.common.net.MediaType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadMapper threadMapper;
    @Resource
    private BoardMapper boardMapper;
    @Resource
    private ReplyMeMapper replyMeMapper;

    @Resource
    private CaptchaService captchaServiceImpl;

    //支持的头像图片类型
    @SuppressWarnings("UnstableApiUsage")
    private static final MediaType[] AVATAR_TYPE = {MediaType.JPEG, MediaType.PNG, MediaType.BMP, MediaType.GIF, MediaType.WEBP};
    //头像保存位置
    private static final String AVATAR_PATH = "/srv/bbs/images/avatar/";
    //缓存用户(id-用户信息)的key
    private static final String USER_CACHE_KEY = "user";

    @Override
    public Status signin(SigninDTO dto) {

        Subject subject = SecurityUtils.getSubject();

        if (!isSignin()) {
            UsernamePasswordToken token =
                    new UsernamePasswordToken(dto.getEmail(), dto.getPassword(), dto.isRemember());
            subject.login(token);
        }
        return ResultUtil.info(StatusEnum.SUCCESS);

    }


    @Override
    public Status signup(SignupDTO dto) throws ArgsNotValidException {
        captchaServiceImpl.checkCaptcha(dto.getEmail(), dto.getCaptcha());
        //创建一个8位字符串的盐值
        String salt = UUID.randomUUID().toString().substring(0, 8);
        dto.setSalt(salt);

        //对密码加盐md5加密
        Md5Hash md5Hash = new Md5Hash(dto.getPassword(), salt);
        dto.setPassword(md5Hash.toHex());


        userMapper.addUser(dto);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    @Override
    public Result<AccountVO> getAccount(Integer userId) throws NotFoundException {


        //true为获取登录用户信息，false为获取其他用户公开信息
        boolean flag = false;
        if (isSignin()) {
            int signUserId = getUserId();
            if (userId == null) {
                userId = signUserId;
                flag = true;
            } else {
                flag = userId.equals(signUserId);
            }
        } else if (!isSignin() && userId == null) {
            throw new AuthenticationException("未登录");
        }

        UserDO user = userMapper.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("用户不存在");
        }


        if (!flag) {
            if (!user.isEmailPublic()) {
                user.setEmail("");
            }
            if (!user.isTelPublic()) {
                user.setTel("");
            }
        }
        AccountVO accountVO = new AccountVO();

        EntityUtil.CopyToEntity(user, accountVO);

        return ResultUtil.success(accountVO);
    }

    @Override
    public Result<List<MyThreadVO>> listMyThread() {
        int userId = getUserId();

        List<ThreadDO> threadDOList = threadMapper.listMyThread(userId);
        Set<Integer> boardIds = threadDOList.stream().map(ThreadDO::getBoardId).collect(Collectors.toSet());
        Map<Integer, BoardDO> boardDOMap = boardMapper.listBoardInBoardIds(boardIds);

        List<MyThreadVO> myThreadVOList = new ArrayList<>();
        for (ThreadDO threadDO : threadDOList) {
            BoardDO boardDO = boardDOMap.get(threadDO.getBoardId());
            if (boardDO != null) {
                MyThreadVO myThreadVO = new MyThreadVO();
                EntityUtil.CopyToEntity(threadDO, myThreadVO);
                myThreadVO.setBoardName(boardDO.getBoardName());
                myThreadVOList.add(myThreadVO);
            }
        }
        return ResultUtil.success(myThreadVOList);
    }

    @Override
    public Result<UserVO> getUser() {
        return ResultUtil.success(getUserSimpleInfo());
    }

    /**
     * 获取登录用户的简单信息
     *
     * @return 用户简单信息
     */
    @Override
    public UserVO getUserSimpleInfo() {
        if (!isSignin()) {
            return null;
        }

        //获取redis中保存的登录用户信息
        UserVO userVO = (UserVO) RedisUtil.hget(USER_CACHE_KEY, getSigninPrincipal());

        if (userVO == null) {

            UserDO user = userMapper.getUserByEmail(getSigninPrincipal());

            userVO = new UserVO();

            EntityUtil.CopyToEntity(user, userVO);

            RedisUtil.hset(USER_CACHE_KEY, getSigninPrincipal(), userVO);
        }

        int count = replyMeMapper.countReplyMe(userVO.getUserId());

        userVO.setMsgCount(count);

        return userVO;
    }


    @Override
    public Status logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    @Override
    public Status updateAccount(UpdateAccountDTO dto) {


        boolean b = EntityUtil.fieldsIsNull(dto);
        if (b) {
            throw new IllegalArgumentException("请添加参数");
        }

        int userId = getUserId();
        //如果其中一个密码有数据，另一个为空或null
        if (StringUtils.hasLength(dto.getNewPassword()) ^ StringUtils.hasLength(dto.getOldPassword())) {
            throw new IllegalArgumentException("请补全密码信息");
        }
        //两个密码都有数据
        if (StringUtils.hasLength(dto.getNewPassword()) && StringUtils.hasLength(dto.getOldPassword())) {

            UserDO user = userMapper.getUserById(userId);

            String oldPassword = new Md5Hash(dto.getOldPassword(), user.getSalt()).toHex();
            if (!oldPassword.equals(user.getPassword())) {
                throw new IllegalArgumentException("旧密码错误");
            }
            String newPassword = new Md5Hash(dto.getNewPassword(), user.getSalt()).toHex();
            dto.setNewPassword(newPassword);
        }

        dto.setUserId(userId);

        RedisUtil.hdel(USER_CACHE_KEY, getSigninPrincipal());
        userMapper.updateAccount(dto);


        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    @Override
    public Status avatarSetting(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String fileName = FileUploadUtil.mediaTypeCheck(file, AVATAR_TYPE);

        FileUploadUtil.saveFileInLocal(file, AVATAR_PATH, fileName);
        userMapper.updateAvatarByEmail(getSigninPrincipal(), fileName);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    @Override
    public String getSigninPrincipal() {
        String principal = null;
        if (isSignin()) {
            principal = SecurityUtils.getSubject().getPrincipal().toString();
        }
        return principal;
    }


    @Override
    public Status signupSendCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException {
        //判断邮箱是否存在
        boolean hasEmail = userMapper.hasEmail(dto.getEmail());
        if (hasEmail) {
            throw new ArgsNotValidException("该邮箱已存在");
        }
        captchaServiceImpl.sendCaptchaAndSave(dto.getEmail());
        return ResultUtil.success();
    }

    @Override
    public Status resetPasswordCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException {
        boolean hasEmail = userMapper.hasEmail(dto.getEmail());
        if (!hasEmail) {
            throw new ArgsNotValidException("邮箱不存在");
        }
        captchaServiceImpl.sendCaptchaAndSave(dto.getEmail());


        return ResultUtil.success();
    }

    @Override
    public boolean isSignin() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated() != subject.isRemembered();
    }


    @Override
    public int getUserId() {
        if (!isSignin()) {
            throw new AuthenticationException("未登录");
        }
        UserVO user = getUserSimpleInfo();
        return user == null ? 0 : user.getUserId();
    }

    @Override
    public Status resetPassword(ResetPasswordDTO dto) throws ArgsNotValidException {

        captchaServiceImpl.checkCaptcha(dto.getEmail(), dto.getCaptcha());

        String salt = UUID.randomUUID().toString().substring(0, 8);
        String password = new Md5Hash(dto.getPassword(), salt).toHex();
        userMapper.updatePasswordByEmail(dto.getEmail(), password, salt);

        return ResultUtil.success();
    }


}