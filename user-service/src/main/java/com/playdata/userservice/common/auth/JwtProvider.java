package com.playdata.userservice.common.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * jwt 토큰 발행
     * @param id 토큰에 심을 유저 정보
     * @param role 토큰에 저장할 유저 롤
     * @return token 발행
     */
    public String createToken(String id, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (expiration * 1000 * 60L));
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * @param token Bearer가 제거된 토큰
     * @return 토큰의 서명이 유효하면 true 유효하지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

    /**
     * @param token Bearer가 제거된 토큰
     * @return token에서 Claim 객체 추출. 예외가 발생하면 null 반환!
     */
    public Claims getClaim(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // 토큰 만료
            log.error("token expired");
        } catch (SignatureException e) { // 서명 오류
            log.error("token signature error");
        } catch (Exception e) {
            log.error("token error");
        }

        return claims;
    }
}
