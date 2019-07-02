package com.ahao.spring.boot.jwt;

import com.ahao.spring.boot.Starter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = Starter.class)
class JwtTest {

    @Autowired
    protected WebApplicationContext wac;

    @Test
    void verify() {
        long now = System.currentTimeMillis();

        // 1. 生成 jwt
        SecretKey key = MacProvider.generateKey();
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
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        String token = mockMvc.perform(post("/jwt")
            .param("username", "admin")
            .param("password", "pw"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        System.out.println("获取token: " + token);


        String responseString = mockMvc.perform(get("/test")
            .header("Authorization", token)
            .param("msg", "hello"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        Assertions.assertEquals("接收:hello", responseString);
    }

    @Test
    void failByNoToken() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String responseString = mockMvc.perform(get("/test")
            .param("msg", "hello"))
            .andExpect(status().is5xxServerError())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        Assertions.assertEquals("{\"result\":0,\"msg\":\"获取 token 失败, 请重新获取 token\",\"obj\":null}", responseString);
    }

    @Test
    void failByErrorPassword() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        String token = mockMvc.perform(post("/jwt")
            .param("username", "admin")
            .param("password", "error"))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
        System.out.println("获取token: " + token);
        Assertions.assertEquals("", token);
    }
}
