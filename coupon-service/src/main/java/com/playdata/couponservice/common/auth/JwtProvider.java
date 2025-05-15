package com.playdata.couponservice.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

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

            // 발생 가능한 에러 목록
//      } catch (IllegalArgumentException e) {
//            throw new RuntimeException(e);
//      } catch (UnsupportedJwtException e) {
//            throw new RuntimeException(e);
//      } catch (MalformedJwtException e) {
//            throw new RuntimeException(e);
        }

        return claims;
    }
}
