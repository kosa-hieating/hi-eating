package kr.or.hieating.global.apiPayload.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import kr.or.hieating.global.apiPayload.ApiResponse;
import kr.or.hieating.global.apiPayload.code.ErrorReasonDTO;
import kr.or.hieating.global.apiPayload.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  // @RequestParam  이나  @PathVariable  검증 에러 처리
  @ExceptionHandler
  public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
    String errorMessage =
        e.getConstraintViolations().stream()
            .map(constraintViolation -> constraintViolation.getMessage())
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException("ConstraintViolationException 추출 중 에러가 발생했습니다."));

    try {
      ErrorStatus errorStatus = ErrorStatus.valueOf(errorMessage);
      return handleExceptionInternalConstraint(e, errorStatus, HttpHeaders.EMPTY, request);
    } catch (IllegalArgumentException iae) {
      ApiResponse<Object> body =
          ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), errorMessage, null);
      return super.handleExceptionInternal(
          e, body, HttpHeaders.EMPTY, ErrorStatus._BAD_REQUEST.getHttpStatus(), request);
    }
  }

  // @RequestBody  객체(DTO) 검증 에러 처리
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    Map<String, String> errors = new LinkedHashMap<>();

    e.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError -> {
              String fieldName = fieldError.getField();
              String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
              errors.put(fieldName, errorMessage);
            });

    ApiResponse<Object> body =
        ApiResponse.onFailure(
            ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), errors);

    return handleExceptionInternal(e, body, headers, status, request);
  }

  // 미처 예상하지 못한 에러 처리
  @ExceptionHandler
  public ResponseEntity<Object> exception(Exception e, WebRequest request) {
    log.error("Unhandled Exception: ", e);
    return handleExceptionInternalFalse(
        e,
        ErrorStatus._INTERNAL_SERVER_ERROR,
        HttpHeaders.EMPTY,
        ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(),
        request);
  }

  // 개발자가 의도적으로 발생시킨 예외 처리
  @ExceptionHandler(value = GeneralException.class)
  public ResponseEntity<Object> onThrowException(
      GeneralException generalException, HttpServletRequest request) {
    ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
    return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
  }

  private ResponseEntity<Object> handleExceptionInternal(
      Exception e, ErrorReasonDTO reason, HttpHeaders headers, HttpServletRequest request) {
    ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);
    WebRequest webRequest = new ServletWebRequest(request);
    HttpHeaders httpHeaders = Optional.ofNullable(headers).orElse(HttpHeaders.EMPTY);
    return super.handleExceptionInternal(e, body, httpHeaders, reason.getHttpStatus(), webRequest);
  }

  private ResponseEntity<Object> handleExceptionInternalConstraint(
      Exception e, ErrorStatus errorStatus, HttpHeaders headers, WebRequest request) {
    ApiResponse<Object> body =
        ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
    return super.handleExceptionInternal(e, body, headers, errorStatus.getHttpStatus(), request);
  }

  private ResponseEntity<Object> handleExceptionInternalFalse(
      Exception e,
      ErrorStatus errorStatus,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ApiResponse<Object> body =
        ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
    return super.handleExceptionInternal(e, body, headers, status, request);
  }
}
