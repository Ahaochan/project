package moe.ahao.spring.boot.shiro;

import moe.ahao.domain.entity.AjaxDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 使用 Nginx 对 /login 的 GET 请求做拦截, 指定到静态页面.
     * 其他请求走服务端.
     * <pre>
     * http {
     *     upstream tomcats {
     *         server 192.168.0.100:8080;
     *     }
     *     server {
     *         location /login {
     *             if ($request_method != GET ) {
     *                 proxy_pass http://tomcats;
     *             }
     *             root      /usr/share/nginx/html;
     *             try_files $uri $uri.html login.html;
     *         }
     *     }
     * }
     * </pre>
     */
    @GetMapping("/login")
    @Deprecated
    public String login() {
        return "login.html";
    }

    @PostMapping("/login")
    public Object passwordLogin(@RequestParam String username, @RequestParam String password,
                                @RequestParam(defaultValue = "false") Boolean rememberMe) {
        UsernamePasswordToken passwordToken = new UsernamePasswordToken(username, password, rememberMe);

        try {
            doLogin(passwordToken);
        } finally {
            passwordToken.clear();
        }

        if(SecurityUtils.getSubject().isAuthenticated()) {
            // TODO 更新最后登录时间和最后登录IP
            return AjaxDTO.success("登录成功");
        }
        return AjaxDTO.failure("登录失败");
    }

    @GetMapping("/logout")
    public AjaxDTO logout() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return AjaxDTO.failure("当前没有登陆用户, 退出失败");
        }

        subject.logout(); // TODO session 会销毁，在SessionListener监听session销毁，清理权限缓存
        return AjaxDTO.success("退出成功");
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "没有权限";
    }


    /**
     * 保证登录前后 Session 不同的临时解决方案
     * @param token 验证token
     * @see <a href="https://issues.apache.org/jira/browse/SHIRO-170">Force New Session ID on Authentication</a>
     */
    private Subject doLogin(AuthenticationToken token) {
        // https://issues.apache.org/jira/browse/SHIRO-170
        // 1. 获取旧的session的相关属性
        Subject subject = SecurityUtils.getSubject();
        Session oldSession = subject.getSession();
        Serializable oldSessionId = oldSession.getId();
        LinkedHashMap<Object, Object> attributes = oldSession.getAttributeKeys().stream()
                .collect(LinkedHashMap::new,
                        (map, key) -> map.put(key, oldSession.getAttribute(key)),
                        Map::putAll);

        // 2. 销毁旧的session
        oldSession.stop();

        // 3. 登录创建新的session
        subject.login(token);

        // 4. 恢复旧session的相关属性
        Session newSession = subject.getSession();
        attributes.forEach(newSession::setAttribute);

        logger.debug("OWASP session fixation from {} to {}", oldSessionId, newSession.getId());
        return subject;
    }
}
