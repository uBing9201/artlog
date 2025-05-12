package com.playdata.gatewayservice.filter;

import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.security.auth.Subject;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<Object> {
    @Value("${jwt.secretKey}")
    private String secretKey;


    /**
     *
     * @param config 내부에서 제작한 Config 클래스 (현재 내부 필드 없음)
     * @return token의 유무와 정상적인 token인지 확인하는 Filter
     */
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();



            if(!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No Authorization Header");
            }

            String authHeader = request.getHeaders().get("Authorization").get(0);
            if(!authHeader.startsWith("Bearer ")) {
                return onError(exchange, "No Bearer Token");
            }

            String token = authHeader.substring(7);

            if(!isTokenValid(token)) {
                return onError(exchange, "Invalid Bearer Token");
            }

            return chain.filter(exchange);
        };
    }

    /**
     *
     * @param token Bearer 제거한 token 값
     * @return token 내부 subject가 정상적으로 등록되어 있는지 여부
     */
    private boolean isTokenValid(String token) {
        String subject = null;
        try {
             subject = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error("토큰 인증 과정에서 오류 발생");
        }

        return !Strings.isBlank(subject);
    }

    /**
     * @param exchange WebFlux 기반 에러 처리를 위한 exchange
     * @param message 에러 발생 시 에러 구분을 위한 에러 메시지
     * @return gateway를 통과하지 않고 클라이언트에게 에러 전송
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.error(new NotAuthorizedException(message));
    }

}
