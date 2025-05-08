package com.playdata.userservice.common.filter;

import com.playdata.userservice.common.auth.JwtProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    /**
     * @param request JwtAuthFilter에 들어오는 요청
     * @param response 다음 필터로 넘길 응답
     * @param filterChain 다음 필터로 요청을 넘기기 위한 filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // 토큰이 유효한지 다시 한번 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Token invalid");
                filterChain.doFilter(request, response);
                return;
        }

        // 토큰 까서 SecurityContextHolder에 username 등록
        String token = authHeader.replace("Bearer ", "");
        Claims claim = jwtProvider.getClaim(token);
        if (claim != null) {
            String username = claim.getSubject();
            // ROLE 생기면 추가
//             claim.get("role", String.class);

            // SecurityContextHolder에 username 추가
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token invalid");
        }

        filterChain.doFilter(request, response);
    }
}
