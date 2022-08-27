package com.example.app.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom Exception
 */
@Getter
@AllArgsConstructor
public enum ExceptionType {

	/* 400 BAD_REQUEST : 잘못된 요청 */
	INVALID_PASSWORD(BAD_REQUEST, "올바르지 않은 비밀번호 입니다."),
	INVALID_TOKEN_TYPE(BAD_REQUEST, "올바르지 않은 토큰 타입 입니다."),
	INVALID_REFRESH_TOKEN(BAD_REQUEST, "사용할 수 없는 토큰 입니다."),

	/* 403 FORBIDDEN : Resource 접근 권한 없음 */
	ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

	/* 404 NOT_FOUND : Resource 찾을 수 없음 */
	USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
	REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "리프레쉬 토큰을 찾을 수 없습니다."),

	/* 409 CONFLICT : Resource 현재 상태와 충돌. 보통 중복된 데이터 존재 */
	DUPLICATE_USERNAME(CONFLICT, "이미 존재하는 아이디입니다."),

	/* 500 INTERNAL_SERVER_ERROR */
	FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

	private final HttpStatus httpStatus;

	private String detail;

}
