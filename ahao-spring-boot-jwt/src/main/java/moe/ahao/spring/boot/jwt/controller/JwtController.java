package moe.ahao.spring.boot.jwt.controller;

import moe.ahao.spring.boot.jwt.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    private static final Logger logger = LoggerFactory.getLogger(JwtController.class);

    @Autowired
    private JwtService jwtService;

    @CrossOrigin
    @PostMapping("/jwt")
    public Object getOneDayToken(String username, String password) {
        boolean validate = "admin".equals(username) && "pw".equals(password);

        if(!validate) {
            logger.error("账号密码错误");
            return null;
        }
        String token = jwtService.createToken("123");
        return token;
    }
}
