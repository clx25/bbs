package com.bbs.service;

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
import com.bbs.mapper.UserMapper;
import com.bbs.util.EntityUtil;
import com.bbs.util.RedisUtil;
import com.bbs.util.ResultUtil;
import com.bbs.util.StatusEnum;
import com.bbs.util.savefile.SaveStrategy;
import com.google.common.net.MediaType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;


    @Resource
    private ServiceMediator serviceMediator;

    //支持的头像图片类型
    @SuppressWarnings("UnstableApiUsage")
    private static final MediaType[] AVATAR_TYPE = {MediaType.JPEG, MediaType.PNG, MediaType.BMP, MediaType.GIF, MediaType.WEBP};
    //头像保存位置
    private static String avatarPath;


    //缓存用户(id-用户信息)的key
    private static final String USER_CACHE_KEY = "user";

    @Value("${avatarPath}")
    public void setAvatarPath(String avatarPath) {
        UserService.avatarPath = avatarPath;
    }


    /**
     * 登录
     *
     * @param dto 登录信息
     * @return 操作状态
     */
    public Status signin(SigninDTO dto) {

        Subject subject = SecurityUtils.getSubject();

        if (!isSignin()) {
            UsernamePasswordToken token =
                    new UsernamePasswordToken(dto.getEmail(), dto.getPassword(), dto.isRemember());
            subject.login(token);
        }
        return ResultUtil.info(StatusEnum.SUCCESS);

    }

    /**
     * 注册
     *
     * @param dto 注册信息
     * @return 操作状态
     * @throws ArgsNotValidException 参数错误
     */
    public Status signup(SignupDTO dto) throws ArgsNotValidException {

        serviceMediator.checkCaptcha(dto.getEmail(), dto.getCaptcha());

        //创建一个8位字符串的盐值
        String salt = UUID.randomUUID().toString().substring(0, 8);
        dto.setSalt(salt);

        //对密码加盐md5加密
        Md5Hash md5Hash = new Md5Hash(dto.getPassword(), salt);
        dto.setPassword(md5Hash.toHex());


        userMapper.addUser(dto);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }


    /**
     * 获取账户信息
     *
     * @param userId 用户id
     * @return 用户信息
     * @throws NotFoundException 找不到用户
     */
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

    /**
     * 获取发布的帖子
     *
     * @return 发布的帖子集合
     */
    public Result<List<MyThreadVO>> listMyThread() {
        int userId = getUserId();

        List<ThreadDO> threadDOList = serviceMediator.listMyThread(userId);
        //获取帖子所在版块id
        Set<Integer> boardIds = threadDOList.stream().map(ThreadDO::getBoardId).collect(Collectors.toSet());
        List<BoardDO> boardDOList = serviceMediator.listBoardInBoardIds(boardIds);

        List<MyThreadVO> myThreadVOList = new ArrayList<>();

        for (ThreadDO threadDO : threadDOList) {
            for (BoardDO boardDO : boardDOList) {
                if (threadDO.getBoardId() == boardDO.getId()) {
                    MyThreadVO myThreadVO = new MyThreadVO();
                    EntityUtil.CopyToEntity(threadDO, myThreadVO);
                    myThreadVO.setBoardName(boardDO.getBoardName());
                    myThreadVOList.add(myThreadVO);
                }
            }
        }
        return ResultUtil.success(myThreadVOList);
    }

    /**
     * 获取简单的用户信息
     *
     * @return 用户信息
     */
    public Result<UserVO> getUser() {
        return ResultUtil.success(getUserSimpleInfo());
    }

    /**
     * 获取登录用户的简单信息
     *
     * @return 用户简单信息
     */
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

        int count = serviceMediator.countReplyMe(userVO.getUserId());

        userVO.setMsgCount(count);

        return userVO;
    }

    /**
     * 退出
     *
     * @return 操作状态
     */
    public Status logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 修改账户信息
     *
     * @param dto 账户信息
     * @return 操作状态
     */
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

    /**
     * 设置头像
     *
     * @param file         头像文件
     * @param saveStrategy 保存策略
     * @return 操作状态
     * @throws IOException              IOException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public Status avatarSetting(MultipartFile file, SaveStrategy saveStrategy) throws IOException, NoSuchAlgorithmException {

        String fileType = saveStrategy.mediaTypeCheck(file, AVATAR_TYPE);
        String fileHash = saveStrategy.fileHash(file);
        String fileName = fileHash + "." + fileType;

        saveStrategy.save(file, avatarPath, fileName);

        userMapper.updateAvatarByEmail(getSigninPrincipal(), fileName);

        return ResultUtil.info(StatusEnum.SUCCESS);
    }

    /**
     * 获取登录后的主体信息
     *
     * @return 登录的邮箱
     */
    public String getSigninPrincipal() {
        String principal = null;
        if (isSignin()) {
            principal = SecurityUtils.getSubject().getPrincipal().toString();
        }
        return principal;
    }

    /**
     * 注册时发送验证码
     *
     * @param dto 注册信息
     * @return 操作状态
     * @throws ArgsNotValidException ArgsNotValidException
     */
    public Status signupSendCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException {
        //判断邮箱是否存在
        boolean hasEmail = userMapper.hasEmail(dto.getEmail());
        if (hasEmail) {
            throw new ArgsNotValidException("该邮箱已存在");
        }
        serviceMediator.sendCaptchaAndSave(dto.getEmail());
        return ResultUtil.success();
    }

    /**
     * 重置密码时发送验证码
     *
     * @param dto 重置密码信息
     * @return 操作状态
     * @throws ArgsNotValidException ArgsNotValidException
     */
    public Status resetPasswordCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException {
        boolean hasEmail = userMapper.hasEmail(dto.getEmail());
        if (!hasEmail) {
            throw new ArgsNotValidException("邮箱不存在");
        }
        serviceMediator.sendCaptchaAndSave(dto.getEmail());


        return ResultUtil.success();
    }

    /**
     * 是否已登录或已记住
     *
     * @return 是否已登录或已记住
     */
    public boolean isSignin() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated() != subject.isRemembered();
    }

    /**
     * 获取登录的用户id
     *
     * @return 用户id
     */
    public int getUserId() {
        if (!isSignin()) {
            throw new AuthenticationException("未登录");
        }
        UserVO user = getUserSimpleInfo();
        return user == null ? 0 : user.getUserId();
    }

    /**
     * 重置密码
     *
     * @param dto 重置密码信息
     * @return 操作状态
     * @throws ArgsNotValidException ArgsNotValidException
     */
    public Status resetPassword(ResetPasswordDTO dto) throws ArgsNotValidException {

        serviceMediator.checkCaptcha(dto.getEmail(), dto.getCaptcha());

        String salt = UUID.randomUUID().toString().substring(0, 8);
        String password = new Md5Hash(dto.getPassword(), salt).toHex();
        userMapper.updatePasswordByEmail(dto.getEmail(), password, salt);

        return ResultUtil.success();
    }

    /**
     * 更具用户id集合获取用户集合
     *
     * @param userIds 用户id集合
     * @return 用户集合
     */
    List<UserDO> listUserByIds(Set<Integer> userIds) {
        return userMapper.listUserByIds(userIds);
    }

}