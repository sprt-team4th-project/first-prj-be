package com.example.commercepilot.web.filter;

import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.web.AdminUserDetails;
import com.example.commercepilot.web.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
//    private static final List<String> WHITE_LIST = List.of(
//            "/api/admins/login"
//    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 토큰 존재 검증
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
//            log.info("JWT 토큰이 필요합니다.");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유효성 검증
        String jwt = authorization.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\":\"invalid_token\"}");
            return;
        }

        // 정보 추출
        Long id = jwtUtil.extractId(jwt);
        String email = jwtUtil.extractEmail(jwt);
        AdminRole role = AdminRole.valueOf(jwtUtil.extractRole(jwt));

//        request.setAttribute("username", username);
//        Spring Security 방식으로 전환(Spring Security에서 제공하는 User 객체 사용)
//        User user = new User(email, "", List.of(new SimpleGrantedAuthority(role.getRole())));

//        id를 포함한 JWT 토큰 사용하기
        AdminUserDetails adminUserDetails = new AdminUserDetails(
                id, email, List.of(new SimpleGrantedAuthority(role.getRole())));

        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities()));

        log.info("인증 정보: {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("권한: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        // 위 SecurityContextHolder에 담고 SecurityContextHolderAwareRequestFilter에서 인가 처리 진행
        filterChain.doFilter(request, response);

    }

    // 화이트 리스트에 속한 주소인 경우, 토큰 검사를 하지 않아도 통과
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return WHITE_LIST.contains(request.getRequestURI());
//    }
}
