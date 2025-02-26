//package org.example.expert.config.interceptor;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.expert.config.JwtUtil;
//import org.example.expert.domain.user.enums.UserRole;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class AuthInterceptor implements HandlerInterceptor {
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        log.info("preHandle 시작.");
//        String url = request.getRequestURI();
//        String method = request.getMethod();
//
//        boolean is_Admin = checkAdmin(request);
//
//        if(!is_Admin) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "관리자 권한이 필요");
//            return false;
//        }
//        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
//        log.info("관리자 권한 접근 허용 - URL: {}, 방식: {}, 시간:{}", url, method, date);
//        return true;
//    }
//
//    public boolean checkAdmin(HttpServletRequest request) {
//        try {
//            String authHeader = request.getHeader("Authorization");
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String token = jwtUtil.substringToken(authHeader);
//                Claims claims = jwtUtil.extractClaims(token);
//
//                UserRole userRole = UserRole.valueOf(claims.get("UserRole", String.class));
//                return UserRole.ADMIN == userRole;
//            }
//        } catch (Exception e) {
//            log.error("관리자 권한 체크 에러", e);
//        }
//        return false;
//    }
//}
