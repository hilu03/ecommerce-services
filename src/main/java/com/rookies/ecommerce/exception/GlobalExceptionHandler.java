package com.rookies.ecommerce.exception;

import com.rookies.ecommerce.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleGlobalException(Exception exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(ErrorCode.UNKNOWN_ERROR.getHttpStatusCode())
                .body(new APIResponse(ErrorCode.UNKNOWN_ERROR.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException exception) {
        log.error("Exception: {}", exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatusCode())
                .body(new APIResponse(exception.getErrorCode().getMessage(), null));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    ResponseEntity<APIResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error("Exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse("Invalid request data!", null));
    }

}
