package moe.ahao.spring.boot.jwt.service;

import io.jsonwebtoken.*;

import java.util.Collections;
import java.util.Map;

public interface JwtService {
    default String createToken() {
        return createToken((Map<String, Object>) null);
    }

    default String createToken(String userId) {
        return createToken(Collections.singletonMap("userId", userId));
    }

    String createToken(Map<String, Object> claims);

    Jws<Claims> parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
}
