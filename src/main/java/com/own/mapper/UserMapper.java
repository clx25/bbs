package com.own.mapper;

import com.own.entity.database.UserDO;
import com.own.entity.dto.SignupDTO;
import com.own.entity.dto.UpdateAccountDTO;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;
import java.util.Set;


public interface UserMapper {



    /**
     * 添加用户
     * @param dto 用户数据
     */
    void addUser(SignupDTO dto);



    /**
     * 查看邮箱是否已存在
     * @param email 邮箱
     * @return true为已存在，false为不存在
     */
    boolean hasEmail(String email);



    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户数据
     */
    UserDO getUserByEmail(String email);


    /**
     * 通过用户id获取用户信息
     * @param userId 用户id
     * @return 用户数据
     */
    UserDO getUserById(int userId);



    /**
     * 修改用户信息
     * @param dto 用户数据
     */
    void updateAccount(UpdateAccountDTO dto);




    /**
     * 根据用户id修改头像
     * @param email 邮箱
     * @param fileName 头像文件名
     */
    void updateAvatarByEmail(String email, String fileName);


    /**
     * 根据用户id集合从redis获取用户数据
     *
     * @param userIds 用户id集合
     * @return 用户信息map，key是用户id,value是用户信息
     */
    @MapKey("id")
    Map<Integer, UserDO> listUserByIds(Set<Integer> userIds);


    /**
     * 根据邮箱修改用户密码
     * @param email 邮箱
     * @param password 密码
     */
    void updatePasswordByEmail(String email, String password,String salt);
}
