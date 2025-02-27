package org.example.expert.config.aop;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.exception.CommonExceptionStatus;
import org.example.expert.domain.common.exception.CommonExceptions;
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
    public void trackAdminLog() throws IOException {
        boolean UserisAdmin = cheackAdmin(request);

        if (!UserisAdmin) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "관리자 권한이 필요합니다.");
        }
        String authorization = request.getHeader("Authorization");
        String token = jwtUtil.substringToken(authorization);
        Claims claims = jwtUtil.extractClaims(token);

        String userId = claims.getSubject(); // 사용자 ID
        String url = request.getRequestURI(); // URL
        String method = request.getMethod(); // METHOD
        String acessTimeDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE); // 시간

        log.info("관리자 접근: 사용자 ID: {}, URL: {}, 방식: {}, 접근한 시간: {}", userId, url, method, acessTimeDate);

    }

    public boolean cheackAdmin(HttpServletRequest request) {
        String adminHeader = request.getHeader("Authorization");

        if (adminHeader != null && adminHeader.startsWith("Bearer ")) { // adminHeader가 null이거나 adminHeader가 "Bearer "으로 시작할때
            String token = jwtUtil.substringToken(adminHeader);
            Claims claims = jwtUtil.extractClaims(token);

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class)); // 대소문자때문에 오류가 발생함
            return UserRole.ADMIN == userRole;
        }
        else {
            throw new CommonExceptions(CommonExceptionStatus.ADMIN_UNAUTHORIZED);
        }
    }
}
