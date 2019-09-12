package com.ahao.spring.boot.jwt.service.impl;

import com.ahao.spring.boot.jwt.config.JwtProperties;
import com.ahao.spring.boot.jwt.service.JwtService;
import com.ahao.util.commons.lang.reflect.ReflectHelper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    private JwtProperties jwtProperties;

    public String createToken(Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder();
        // 1. 设置 header, 加密算法, 压缩算法
        SignatureAlgorithm signatureAlgorithm = jwtProperties.getSignatureAlgorithm();
        String signKey = jwtProperties.getSignKey();
        builder.signWith(signatureAlgorithm, signKey);
        if (jwtProperties.getCompressionCodec() != null) {
            builder.compressWith(ReflectHelper.create(jwtProperties.getCompressionCodec()));
        }
        // 2. 设置自定义 header
        Map<String, Object> customHeader = jwtProperties.getCustomHeader();
        builder.setHeaderParams(customHeader);

        // 3. 设置参数体
        builder.setId(UUID.randomUUID().toString()) // JWT_ID
            .setAudience("")                        // 接受者
            .setSubject("")                         // 主题
            .setIssuer("")                          // 签发者
            .addClaims(claims);                     // 自定义参数

        // 4. 设置时间参数
        builder.setIssuedAt(new Date(now)) // 签发时间
            .setNotBefore(new Date(now))   // 失效时间
            .setExpiration(new Date(now + jwtProperties.getExpiration())); // 过期时间
        String token = builder.compact();
        return token;
    }

    public Jws<Claims> parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        String signKey = jwtProperties.getSignKey();
        return Jwts.parser()
            .setSigningKey(signKey)
            .parseClaimsJws(token);
    }
}
