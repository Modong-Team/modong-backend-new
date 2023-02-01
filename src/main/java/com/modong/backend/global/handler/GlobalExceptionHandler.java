package com.modong.backend.global.handler;

import com.modong.backend.base.Dto.ErrorResponse;
import com.modong.backend.global.exception.BadRequestException;
import com.modong.backend.global.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  private static final String ERROR_LOGGING_MESSAGE = "예외 발생: ";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  public ErrorResponse handleBadRequestException(final BadRequestException e) {
    log.error(ERROR_LOGGING_MESSAGE, e);
    return new ErrorResponse(e.getErrorCode(), e.getClientMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorResponse handleNotFoundException(final NotFoundException e) {
    log.error(ERROR_LOGGING_MESSAGE, e);
    return new ErrorResponse(e.getErrorCode(), e.getClientMessage());
  }
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public void handleRuntimeException(final RuntimeException e) {
    log.error("예상하지 못한 에러가 발생하였습니다.", e);
  }
}
