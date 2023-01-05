package com.tcdt.qlnvhang.config;

import com.google.gson.Gson;
import com.tcdt.qlnvhang.entities.UserActivity;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.service.UserActivityService;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActivityConfig extends HandlerInterceptorAdapter {

    @Autowired
    private UserActivityService userActivityService;
    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        if (ObjectUtils.isEmpty(userAgent)) {
            userAgent = request.getHeader("user-agent");
        }
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
        if (m.find()) {
            userAgent = m.group(1);
        }
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserActivity entity = new UserActivity();
        entity.setIp(UserUtils.getClientIpAddress(request));
        entity.setRequestMethod(request.getMethod());
        entity.setRequestUrl(request.getRequestURI());
        entity.setUserId(user.getUser().getId());
        entity.setUserAgent(userAgent);
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            entity.setRequestParameter(gson.toJson(parameterMap));
        }
        userActivityService.log(entity);
        return super.preHandle(request, response, handler);
    }


}
