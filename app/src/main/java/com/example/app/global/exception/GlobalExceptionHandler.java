package com.example.app.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler
 * @author Changhyun Soun
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/* 400 START */

	/**
	 * Validation fail
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("MethodArgumentNotValidException :: ", ex);

		List<String> errors = generateErrors(ex);
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), errors);

		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/***
	 * ModelAttribute binding error
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(
		BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("BindException :: ", ex);

		List<String> errors = generateErrors(ex);
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/**
	 * Bean type error
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
		TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("TypeMismatchException :: ", ex);

		String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), error);
		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/**
	 * multipart/form-data fail
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(
		MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("MissingServletRequestPartException :: ", ex);

		String error = ex.getRequestPartName() + " part is missing";
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), error);
		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/**
	 * Required Parameter(Argument) loss
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("MissingServletRequestParameterException :: ", ex);

		String error = ex.getParameterName() + " parameter is missing";
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), error);
		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/**
	 * Parameter(Argument) mismatch
	 */
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		log.error("MethodArgumentTypeMismatchException :: ", ex);

		String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), error);
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Constraint violation
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		log.error("ConstraintViolationException :: ", ex);

		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
		}

		ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/* 400 END */

	/**
	 * Not found 404
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
		NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("NoHandlerFoundException :: ", ex);

		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		ExceptionResponse response = new ExceptionResponse(NOT_FOUND, ex.getLocalizedMessage(), error);
		return ResponseEntity.status(response.getStatus()).headers(headers).body(response);
	}

	/**
	 * HTTP method not supported 405
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("HttpRequestMethodNotSupportedException :: ", ex);

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");

		Set<HttpMethod> supportedHttpMethods = ex.getSupportedHttpMethods();
		if (supportedHttpMethods != null) {
			supportedHttpMethods.forEach(t -> builder.append(t).append(" "));
		}

		ExceptionResponse response = new ExceptionResponse(METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Media type not supported 415
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
		HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("HttpMediaTypeNotSupportedException :: ", ex);

		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

		ExceptionResponse response = new ExceptionResponse(UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Some exceptions with 500
	 */
	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleAll(Exception ex) {
		log.error("Exception :: ", ex);

		ExceptionResponse response = new ExceptionResponse(INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error !");
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Access Denied
	 */
	@ExceptionHandler(value = AccessDeniedException.class)
	protected ResponseEntity<ExceptionResponse> handleAccessDeniedException(Exception ex) {
		log.error("AccessDeniedException :: ", ex);

		ExceptionResponse response = new ExceptionResponse(FORBIDDEN, ex.getLocalizedMessage(), "Access Denied !");
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Bad Credentials
	 */
	@ExceptionHandler(value = BadCredentialsException.class)
	protected ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		log.error("BadCredentialsException :: ", ex);

		ExceptionResponse response = new ExceptionResponse(UNAUTHORIZED, ex.getLocalizedMessage(), "Wrong username or password !");
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Authentication problems
	 */
	@ExceptionHandler(value = AuthenticationException.class)
	protected ResponseEntity<ExceptionResponse> handleAuthenticationException(Exception ex) {
		log.error("AuthenticationException :: ", ex);

		ExceptionResponse response = new ExceptionResponse(UNAUTHORIZED, ex.getLocalizedMessage(), "Please Login !");
		return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders()).body(response);
	}

	/**
	 * Custom Exception occurs
	 */
	@ExceptionHandler(value = com.example.app.global.exception.CustomException.class)
	protected ResponseEntity<ExceptionResponse> customException(com.example.app.global.exception.CustomException ex) {
		log.error("CustomException :: ", ex);

		com.example.app.global.exception.ExceptionType exceptionType = ex.getExceptionType();
		return ResponseEntity.status(exceptionType.getHttpStatus()).body(ExceptionResponse.toExceptionResponse(exceptionType));
	}

	private List<String> generateErrors(BindException ex) {
		List<String> errors = new ArrayList<>();
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

		for (ObjectError error : allErrors) {
			errors.add(error.getDefaultMessage());
		}
		return errors;
	}

}
