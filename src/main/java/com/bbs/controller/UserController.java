package com.bbs.controller;

import com.bbs.entity.Result;
import com.bbs.entity.Status;
import com.bbs.entity.dto.*;
import com.bbs.entity.vo.AccountVO;
import com.bbs.entity.vo.MyThreadVO;
import com.bbs.entity.vo.UserVO;
import com.bbs.exception.custom.ArgsNotValidException;
import com.bbs.exception.custom.NotFoundException;
import com.bbs.service.UserService;
import com.bbs.util.savefile.SaveStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController

@Validated
@Api(tags = "用户控制器")
@AllArgsConstructor
public class UserController {


    private UserService userService;
    private SaveStrategy savedLocally;

    @ApiOperation("登录")
    @PostMapping(value = "/signin")
    public Status signin(@RequestBody @Valid SigninDTO dto) {
        return userService.signin(dto);
    }


    @ApiOperation("注册")
    @PostMapping("/signup")
    public Status signup(@RequestBody @Valid SignupDTO dto) throws ArgsNotValidException {
        return userService.signup(dto);
    }


    @ApiOperation(value = "获取用户资料", notes = "如果userId是登录用户的userId，则获取登录用户的所有资料，其他情况则获取对应用户的公开资料")
    @ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", required = true)
    @GetMapping("/account/{userId}")
    public Result<AccountVO> getAccount(@PathVariable Integer userId) throws NotFoundException {
        return userService.getAccount(userId);
    }


    @ApiOperation(value = "获取登录用户资料")
    @GetMapping("/account")
    public Result<AccountVO> getAccount() throws NotFoundException {
        return userService.getAccount(null);
    }


    @ApiOperation("获取登录用户发布的帖子")
    @GetMapping("/account/myThread")
    public Result<List<MyThreadVO>> listMyThread() {
        return userService.listMyThread();
    }


    @ApiOperation(value = "获取登录用户基本信息", notes = "如果已登录，则返回数据，如果没登录，状态为200，用户信息为null")
    @GetMapping("/user")
    public Result<UserVO> getUser() {
        return userService.getUser();
    }


    @ApiOperation(value = "修改个人资料")
    @PutMapping("/account")
    public Status updateAccount(@RequestBody @Valid UpdateAccountDTO dto) {
        return userService.updateAccount(dto);
    }

    @ApiOperation("重置密码")
    @PutMapping("/password")
    public Status resetPassword(@RequestBody @Valid ResetPasswordDTO dto) throws ArgsNotValidException {
        return userService.resetPassword(dto);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Status logout() {
        return userService.logout();
    }


    @ApiOperation(value = "上传头像")
    @ApiImplicitParam("头像文件")
    @PostMapping("/avatar")
    public Status uploadAvatar(@NotNull(message = "头像文件为空") MultipartFile file) throws Exception {
        return userService.avatarSetting(file, savedLocally);
    }


    @ApiOperation("注册发送验证码")
    @PostMapping("/signup/captcha")
    public Status signupSendCaptcha(@RequestBody @Valid SendCaptchaDTO dto) throws ArgsNotValidException {
        return userService.signupSendCaptcha(dto);
    }

    @ApiOperation("重置密码发送验证码")
    @PostMapping("/user/password/captcha")
    public Status resetPasswordCaptcha(@RequestBody @Valid SendCaptchaDTO dto) throws ArgsNotValidException {
        return userService.resetPasswordCaptcha(dto);
    }

}
