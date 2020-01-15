package com.bbs.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;

@Slf4j
class HttpMethodPathCheck {
    private static PatternMatcher pathMatcher = new AntPathMatcher();

    static boolean check(String path, ServletRequest request) {
        String requestURI = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        String method = WebUtils.toHttp(request).getMethod();
        String[] split = path.split("&&");
        if (split.length > 1) {
            if (!split[1].equalsIgnoreCase(method)) {
                return false;
            }
            path = split[0];
        }
        log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathMatcher.matches(path, requestURI);
    }


}
