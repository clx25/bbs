package com.bbs.config;

import com.bbs.entity.database.FilterChainDO;
import com.bbs.mapper.FilterChainMapper;
import com.bbs.shiro.HttpMethodAnonymousFilter;
import com.bbs.shiro.CustomShiroFilterFactoryBean;
import com.bbs.shiro.HttpMethodUserFilter;
import com.bbs.web.realm.UserSigninRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Resource
    FilterChainMapper filterChainMapper;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        //创建自定义的ShiroFilterFactoryBean
        CustomShiroFilterFactoryBean factoryBean = new CustomShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        //应用启动时从数据库获取filterchain,注入到factoryBean中
        List<FilterChainDO> filterChainList = filterChainMapper.listFilterChain();
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        for (FilterChainDO filterChain : filterChainList) {
            filterChainMap.put(filterChain.getPath(),filterChain.getName());
        }

        factoryBean.setFilterChainDefinitionMap(filterChainMap);

        //设置自定义的支持rest风格的shirofilter
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("user", new HttpMethodUserFilter());
        filterMap.put("anon", new HttpMethodAnonymousFilter());
        factoryBean.setFilters(filterMap);

        return factoryBean;
    }


    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(
            UserSigninRealm realm, CookieRememberMeManager cookieRememberMeManager,
            DefaultWebSessionManager defaultWebSessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(realm);
        securityManager.setRememberMeManager(cookieRememberMeManager);
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
    }


    @Bean
    public UserSigninRealm userSigninRealm() {
        //设置密码加密规则
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1);

        UserSigninRealm realm = new UserSigninRealm();
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    /**
     * cookie管理器和cookie
     *
     * @return cookie管理器
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie();
        cookie.setMaxAge(2592000);
        cookie.setName("rememberMe");
        cookieRememberMeManager.setCookie(cookie);
        return cookieRememberMeManager;
    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //设置session不过期
        defaultWebSessionManager.setGlobalSessionTimeout(-1L);
        return defaultWebSessionManager;
    }



}
