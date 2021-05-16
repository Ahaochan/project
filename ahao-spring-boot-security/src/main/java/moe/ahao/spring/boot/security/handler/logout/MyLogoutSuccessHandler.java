package moe.ahao.spring.boot.security.handler.logout;

import moe.ahao.domain.entity.AjaxDTO;
import moe.ahao.util.commons.io.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        String msg = "注销成功";
        logger.info("注销成功:{}", auth);

        AjaxDTO dto = AjaxDTO.success(msg, auth);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JSONHelper.toString(dto));
    }
}
