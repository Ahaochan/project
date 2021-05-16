package moe.ahao.spring.boot.security.handler.entrypoint;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.util.commons.io.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.debug("未授权用户尝试访问受保护资源, 已拒绝请求");

        AjaxDTO result = AjaxDTO.failure("请登录后重试");

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JSONHelper.toString(result));
    }
}
