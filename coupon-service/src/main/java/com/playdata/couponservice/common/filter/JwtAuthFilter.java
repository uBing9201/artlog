package com.playdata.couponservice.common.filter;

import com.playdata.couponservice.common.auth.JwtProvider;
import com.playdata.couponservice.common.auth.Role;
import com.playdata.couponservice.common.auth.TokenUserInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
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
                filterChain.doFilter(request, response);
                return;
        }

        // 토큰 까서 SecurityContextHolder에 username 등록
        String token = authHeader.replace("Bearer ", "");
        Claims claim = jwtProvider.getClaim(token);
        if (claim != null) {
            Long id = Long.valueOf(claim.getSubject());
            // ROLE 생기면 추가
            String role = claim.get("role", String.class);
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            // SecurityContextHolder에 username 추가
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new TokenUserInfo(id, Role.valueOf(role)), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Token invalid");
        }

        filterChain.doFilter(request, response);
    }
}
