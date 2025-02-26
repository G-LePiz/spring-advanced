package org.example.expert.config.aop;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

import javax.imageio.IIOException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@Aspect
@RequiredArgsConstructor
public class AdminAop {
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))" +
            " || execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void trackLog() {
    } // 경로를 지정한다.

    @Around("trackLog()")
    public boolean trackAdminLog() throws IOException {
        String url = request.getRequestURI();
        String method = request.getMethod();

        boolean UserisAdmin = cheackAdmin(request);

        if (!UserisAdmin) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "관리자 권한이 필요합니다.");
            return false;
        }
        String acessTimeDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        log.info("관리자 접근: URL: {}, 방식: {}, 접근한 시간: {}", url, method, acessTimeDate);
        return true;
    }

    public boolean cheackAdmin(HttpServletRequest request) {
        String adminHeader = request.getHeader("Authorization");

        if (adminHeader == null && adminHeader.startsWith("Bearer ")) { // adminHeader가 null이거나 adminHeader가 "Bearer "일때
            String token = jwtUtil.substringToken(adminHeader);
            Claims claims = jwtUtil.extractClaims(token);

            UserRole userRole = UserRole.valueOf(claims.get("UserRole", String.class));
            return UserRole.ADMIN == userRole;
        }
        else {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }
    }
}
