package moe.ahao.spring.boot.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {
    @Autowired
    FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @GetMapping("/get/{username}")
    public Map<String, ? extends Session> findByUsername(@PathVariable String username, HttpServletRequest request, HttpSession session) {
        Object sessionBrowser = session.getAttribute("username");
        if (sessionBrowser == null) {
            System.out.println("不存在 session，设置 username=" + username);
            session.setAttribute("username", username);
        } else {
            System.out.println("存在 session，username=" + sessionBrowser.toString());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ":" + cookie.getValue());
            }
        }

        Map<String, ? extends Session> sessionMap = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username);
        return sessionMap;
    }
}
