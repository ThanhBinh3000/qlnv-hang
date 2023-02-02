package com.tcdt.qlnvhang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvhang.entities.UserActivity;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.service.UserActivityService;
import com.tcdt.qlnvhang.util.UserUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final static String SYSTEM = "hang";

    @Autowired
    private Gson gson;
    @Autowired
    private UserActivityService userActivityService;

    @Pointcut("within(com.tcdt.qlnvhang..*) && bean(*Controller))")
    public void v3Controller() {
    }

    @Before("v3Controller()")
    public void logBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String userAgent = request.getHeader("User-Agent");
            if (ObjectUtils.isEmpty(userAgent)) {
                userAgent = request.getHeader("user-agent");
            }
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
            if (m.find()) {
                userAgent = m.group(1);
            }
            if (!String.class.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass())) {
                CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UserActivity entity = new UserActivity();
                entity.setIp(UserUtils.getClientIpAddress(request));
                entity.setRequestMethod(request.getMethod());
                entity.setRequestUrl(request.getRequestURI());
                entity.setUserId(user.getUser().getId());
                entity.setSystem(SYSTEM);
                entity.setUserAgent(userAgent);
                entity.setRequestBody(getBody(joinPoint.getArgs()));
                entity.setUserName(user.getUser().getUsername());
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (parameterMap != null && !parameterMap.isEmpty()) {
                    entity.setRequestParameter(gson.toJson(parameterMap));
                }
                userActivityService.log(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBody(Object[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(args);
    }
}
