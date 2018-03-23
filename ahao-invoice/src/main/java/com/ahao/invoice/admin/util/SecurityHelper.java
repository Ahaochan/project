package com.ahao.invoice.admin.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Avalon on 2017/5/9.
 */
public class SecurityHelper {
    private static final Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

    private SecurityHelper() {

    }

    public static User loginUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication auth = context.getAuthentication();
            Object obj = auth.getPrincipal();
            if (obj instanceof UserDetails) {
                return (User) obj;
            }
        } catch (Exception e) {
            logger.error("获取登录的用户信息失败: ", e);
        }
        return null;
    }

    public static String loginUsername() {
        User user = loginUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，
     * 但是经过nginx等反向代理软件后，这个方法会失效。
     * 本方法先从Header中获取X-Real-IP，
     * 如果不存在再从X-Forwarded-For获得第一个IP(用,分割)
     * 如果还不存在则调用Request .getRemoteAddr()。
     */
    public static String getClientIp() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = request.getHeader("X-Real-IP");
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            } else {
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("获取Ip地址失败:", e);
        }
        return "非法Ip";
    }
}
