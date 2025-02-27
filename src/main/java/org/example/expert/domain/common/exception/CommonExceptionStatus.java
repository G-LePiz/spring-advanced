package org.example.expert.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonExceptionStatus {
    USER_DOES_NOT_RESISTERED(HttpStatus.UNAUTHORIZED, "회원가입이 되지않은 유저입니다."),
    PASSWORD_CANNOT_SAME(HttpStatus.UNAUTHORIZED,"신규 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    PASSWORD_IS_WRONG(HttpStatus.NOT_FOUND, "비밀번호가 잘못되었습니다."),
    AUTH_ANOTATION_WRONG(HttpStatus.NOT_FOUND, "@Auth와 AuthUser 타입은 함께 사용되어야 합니다."),
    EMAIL_IS_ALREADY_USED(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    USER_IS_DOES_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "가입되지 않은 유저입니다."),
    WEATHERDATA_LOADING_FAILED(HttpStatus.BAD_REQUEST,"날씨 데이터를 가져오는데 실패했습니다."),
    CANNOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "오늘에 해당되는 날씨 데이터를 찾을 수 없습니다."),
    WEATHERDATA_DOES_NOT_EXISTS(HttpStatus.NOT_FOUND, "날씨 데이터가 없습니다."),
    ADMIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "관리자 권한이 없습니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할일을 찾지못했습니다."),
    USER_CANNOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾지 못했습니다."),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "매니저를 찾을 수 없습니다."),
    TODO_IS_NOT_MANAGER(HttpStatus.UNAUTHORIZED, "해당 일정에 등록된 담당자가 아닙니다."),
    TODO_USER_DOES_NOT_VAILED(HttpStatus.UNAUTHORIZED, "해당 일정을 만든 유저가 유효하지않습니다."),
    TODO_USER_AUTH_USER_DOES_NOT_MATCH(HttpStatus.UNAUTHORIZED, "담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다."),
    TODO_USER_CANNOT_MANAGER(HttpStatus.FORBIDDEN, "일정 작성자는 본인을 담당자로 등록할 수 없습니다.");

    private HttpStatus errorCode;
    private String message;

}
