package com.bbs.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

/**
 * 替换PathMatchingFilterChainResolver，使用自定义的ChainResolver
 */
public class CustomShiroFilterFactoryBean extends ShiroFilterFactoryBean {


    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        //获取父类createInstance()的返回值，替换FilterChainResolver
        AbstractShiroFilter springShiroFilter = super.createInstance();

        FilterChainManager manager = createFilterChainManager();

        HttpMethodPathMatchingFilterChainResolver chainResolver = new HttpMethodPathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);

        springShiroFilter.setFilterChainResolver(chainResolver);
       return springShiroFilter;
    }

}
