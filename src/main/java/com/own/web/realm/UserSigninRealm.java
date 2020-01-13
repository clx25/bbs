package com.own.web.realm;

import com.own.entity.database.UserDO;
import com.own.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;


public class UserSigninRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 用户认证
     *
     * @param token 前端传入的账号密码
     * @return AuthenticationInfo
     * @throws AuthenticationException 数据库没有邮箱数据
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取邮箱
        String email = token.getPrincipal().toString();

        //使用邮箱从数据库获取用户信息
        UserDO user = userMapper.getUserByEmail(email);
        if (user == null) {
            throw new AuthenticationException();

        }

        ByteSource saltBytes = ByteSource.Util.bytes(user.getSalt());
        return new SimpleAuthenticationInfo(email, user.getPassword(), saltBytes, this.getName());
    }
}
