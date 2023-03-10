package com.modong.backend.global.handler;

import com.modong.backend.base.Dto.ErrorResponse;
import com.modong.backend.global.exception.BadRequestException;
import com.modong.backend.global.exception.auth.NoPermissionException;
import com.modong.backend.global.exception.auth.UnAuthorizedException;
import java.security.InvalidParameterException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String ERROR_LOGGING_MESSAGE = "예외 발생";

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    log.error(ERROR_LOGGING_MESSAGE, e);
    final ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.METHOD_NOT_ALLOWED.value())
        .message(e.getMessage()).build();

    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  //@Validated 검증 실패 시 Catch
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidParameterException.class)
  protected ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {
    log.error(ERROR_LOGGING_MESSAGE, e);
    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(e.toString()).build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
      BadRequestException.class,
      MethodArgumentTypeMismatchException.class //enum 타입이 맞지 않을 경우 발생
  })
  public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
    log.error(ERROR_LOGGING_MESSAGE, e);
    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(e.getClientMessage())
        .code(e.getErrorCode()).build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  // 404예외처리 핸들러
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException e){
    log.error(ERROR_LOGGING_MESSAGE, e);

    ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .code("리소스를 찾을 수 없습니다")
            .message(e.getMessage()).build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({
      UnAuthorizedException.class
  })
  public ResponseEntity<ErrorResponse> handleInvalidAuthorization(final UnAuthorizedException e) {

    log.error(ERROR_LOGGING_MESSAGE, e);

    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.UNAUTHORIZED.value())
        .message(e.getClientMessage())
        .code(e.getErrorCode()).build();

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler({
      NoPermissionException.class
  })
  public ResponseEntity<ErrorResponse> handleNoPermission(final NoPermissionException e) {

    log.error(ERROR_LOGGING_MESSAGE, e);

    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.FORBIDDEN.value())
        .message(e.getClientMessage())
        .code(e.getErrorCode()).build();

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
    log.error("예상하지 못한 에러가 발생하였습니다",e);
    ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .message("예상하지 못한 에러가 발생하였습니다.")
            .code("SERVER ERROR").build();
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
