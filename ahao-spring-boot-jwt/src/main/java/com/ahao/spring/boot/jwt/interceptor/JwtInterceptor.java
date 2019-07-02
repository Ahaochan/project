package com.ahao.spring.boot.jwt.interceptor;

import com.ahao.spring.boot.jwt.annotation.Jwt;
import com.ahao.spring.boot.jwt.service.JwtService;
import com.ahao.util.web.RequestHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    private JwtService jwtService;

    public JwtInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        // 1. 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        // 2. 判断是否有 Jwt 注解
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        Jwt jwt = method.getAnnotation(Jwt.class);
        if (jwt == null || !jwt.required()) {
            return true;
        }

        // 3. 获取 token
        String token = RequestHelper.getHeader("Authorization", null, request);
        if (token == null) {
            throw new JwtException("获取 token 失败, 请重新获取 token");
        }

        // 4. 解析token, 异常交由全局异常拦截器处理
        Jws<Claims> jws = jwtService.parseToken(token);
        Claims claims = jws.getBody();

        // 5. 校验
        String userIdStr = claims.get("userId", String.class);
        Integer userId = Integer.valueOf(userIdStr);
        if (userId <= 0) {
            return false;
        }
        return true;
    }
}
