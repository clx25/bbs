package com.own.servie;

import com.own.entity.Result;
import com.own.entity.Status;
import com.own.entity.dto.*;
import com.own.entity.vo.AccountVO;
import com.own.entity.vo.MyThreadVO;
import com.own.entity.vo.UserVO;
import com.own.exception.customexception.ArgsNotValidException;
import com.own.exception.customexception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface UserService {

    /**
     * 用户登录
     *
     * @param dto 从页面传入的登录数据的实体类
     * @return 操作结果消息
     */
    Status signin(SigninDTO dto);


    /**
     * 用户注册
     *
     * @param dto 接收注册页面传入的注册数据实体类
     * @return 操作结果消息
     */
    Status signup(SignupDTO dto) throws ArgsNotValidException;


    /**
     * 获取登录用户的简单信息(id，用户名，头像，消息数量)
     *
     * @return 返回页面的用户数据
     */
    Result<UserVO> getUser();



    /**
     * 退出登录
     *
     * @return 操作结果消息
     */
    Status logout();


    /**
     * 修改账户信息
     *
     * @param dto 页面传入的账户信息
     * @return 操作结果消息
     */
    Status updateAccount(UpdateAccountDTO dto);


    /**
     * 设置用户头像
     *
     * @param file 上传的文件
     * @throws IOException              I/O异常
     * @throws NoSuchAlgorithmException 没有该算法
     */
    Status avatarSetting(MultipartFile file) throws IOException, NoSuchAlgorithmException;


    /**
     * 获取登录用户的用户主体（邮箱）
     *
     * @return 用户主体（邮箱）
     */
    String getSigninPrincipal();


    /**
     * 向邮箱发送验证码用以注册
     *
     * @param dto 发送验证码需要的数据
     * @return 操作结果消息
     */
    Status signupSendCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException;


    /**
     * 判断是否登录或者是否记住
     *
     * @return true是已登录或已记住，false是未登录
     */
    boolean isSignin();


    /**
     * 从redis获取登录用户的id
     *
     * @return 登录用户的id，未登录返回0
     */
    int getUserId();


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 返回页面的用户账户信息
     */
    Result<AccountVO> getAccount(Integer userId) throws NotFoundException;


    /**
     * 获取用户发布的帖子
     * @return 返回页面的用户发布的帖子信息
     */
    Result<List<MyThreadVO>> listMyThread();

    /**
     * 重置密码
     * @param dto 需要的数据实体类
     * @return 操作状态信息
     */
    Status resetPassword(ResetPasswordDTO dto) throws ArgsNotValidException;

    /**
     * 重置密码发送验证码
     * @param dto 发送验证码需要的数据
     * @return 操作结果消息
     */
    Status resetPasswordCaptcha(SendCaptchaDTO dto) throws ArgsNotValidException;
}
