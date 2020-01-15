package com.bbs.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class HttpMethodPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }


        String requestURI = getPathWithinApplication(request);
        String method = WebUtils.toHttp(request).getMethod();


        for (String pathPattern : filterChainManager.getChainNames()) {

            //解析设置的路径，&&前为URI，后面为提交方式
            String[] split = pathPattern.split("&&");
            if(split.length>1){
                if(!split[1].equalsIgnoreCase(method)){
                    continue;
                }
            }
            if (pathMatches(split[0], requestURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }

        return null;
    }
}
