package com.bbs.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.AnonymousFilter;

import javax.servlet.ServletRequest;

@Slf4j
public class HttpMethodAnonymousFilter extends AnonymousFilter {

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        return HttpMethodPathCheck.check(path,request);
    }
}
