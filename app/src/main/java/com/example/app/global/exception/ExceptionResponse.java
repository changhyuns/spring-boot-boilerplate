package com.example.app.global.exception;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Response DTO (Exception)
 * @author Changhyun Soun
 */
@Getter
@EqualsAndHashCode
public class ExceptionResponse {

	private final HttpStatus status;

	private final String message;

	private final List<String> exceptions;

	public ExceptionResponse(HttpStatus status, String message, List<String> exceptions) {
		this.status = status;
		this.message = message;
		this.exceptions = exceptions;
	}

	public ExceptionResponse(HttpStatus status, String message, String exception) {
		this.status = status;
		this.message = message;
		this.exceptions = List.of(exception);
	}

	public static ExceptionResponse toExceptionResponse(com.example.app.global.exception.ExceptionType exceptionType) {
		return new ExceptionResponse(
			exceptionType.getHttpStatus(),
			exceptionType.name(),
			exceptionType.getDetail());
	}
}
