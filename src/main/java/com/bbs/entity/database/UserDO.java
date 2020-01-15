package com.bbs.entity.database;

import com.bbs.annotation.FieldAlias;
import lombok.Data;

import java.util.Date;

/**
 * user表
 */
@Data
public class UserDO {
    //用户id
    @FieldAlias("userId")
    private int id;
    //用户邮箱
    private String email;
    //用户名
    private String username;
    //电话
    private String tel;
    //密码
    private String password;
    //加密使用的盐值
    private String salt;

    private long integral;

    //注册时间
    @FieldAlias("signupDate")
    private Date gmt_create;
    //性别
    private int sex;
    //生日
    private Date birthday;
    //邮箱是否公开
    private boolean emailPublic;
    //电话是否公开
    private boolean telPublic;
    //头像
    private String avatar;
}
