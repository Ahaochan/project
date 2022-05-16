package moe.ahao.spring.boot.jwt;

import io.jsonwebtoken.*;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(classes = Starter.class)
class JwtTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void verify() {
        long now = System.currentTimeMillis();

        // 1. 生成 jwt
        String key = Base64.getEncoder().encodeToString("signKey".getBytes(StandardCharsets.UTF_8));
        JwtBuilder builder = Jwts.builder()
            .setId("id")                // JWT_ID
            .setAudience("audience")    // 接受者
            .setSubject("subject")      // 主题
            .setIssuer("issuer")        // 签发者
            .addClaims(null)            // 自定义属性
            .setIssuedAt(new Date(now))        // 签发时间
            .setNotBefore(new Date(now - 1))   // 生效时间
            .setExpiration(new Date(now + 1000 * 60 * 60))  // 过期时间
            .signWith(SignatureAlgorithm.HS256, key); // 签名算法以及密匙
        String token = builder.compact();
        System.out.println(token);

        // 2. 解析 jwt
        try {
            Jws<Claims> jws = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token);
            JwsHeader header = jws.getHeader();
            Claims claims = jws.getBody();

            Assertions.assertEquals(SignatureAlgorithm.HS256.getValue(), header.getAlgorithm());
            Assertions.assertEquals("id", claims.getId());
            Assertions.assertEquals("audience", claims.getAudience());
            Assertions.assertEquals("subject", claims.getSubject());
            Assertions.assertEquals("issuer", claims.getIssuer());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }

    }

    @Test
    void success() throws Exception {
        String token = mockMvc.perform(post("/jwt")
            .param("username", "admin")
            .param("password", "pw"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();
        System.out.println("获取token: " + token);

        String msg = "hello";
        mockMvc.perform(get("/test")
            .header("Authorization", token)
            .param("msg", msg))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("接收:" + msg));

    }

    @Test
    void failByNoToken() throws Exception {
        mockMvc.perform(get("/test")
            .param("msg", "hello")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.code").value(1))
            .andExpect(jsonPath("$.msg").value("获取 token 失败, 请重新获取 token"))
            .andExpect(jsonPath("$.obj").isEmpty());
    }

    @Test
    void failByErrorPassword() throws Exception {
        mockMvc.perform(post("/jwt")
            .param("username", "admin")
            .param("password", "error"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }
}
