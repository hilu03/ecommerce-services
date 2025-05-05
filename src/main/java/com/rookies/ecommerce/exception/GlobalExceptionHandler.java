package com.rookies.ecommerce.exception;

import com.rookies.ecommerce.dto.response.APIResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.jsonwebtoken.security.SignatureException;
import java.util.Objects;

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
        if (exception.getMessage().contains("email")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage(), null));
        }
        else if (exception.getMessage().contains("reviews")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse(ErrorCode.ALREADY_REVIEWD.getMessage(), null));
        }
        else if (exception.getMessage().contains("categories")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse(ErrorCode.CATEGORY_NAME_EXISTED.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse("Invalid request data!", null));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<APIResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(ErrorCode.RESOURCE_NOT_FOUND.getHttpStatusCode())
                .body(new APIResponse(ErrorCode.RESOURCE_NOT_FOUND.getMessage(), exception.getMessage()));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<APIResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new APIResponse("Method not allowed", exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse("Invalid request data",
                        Objects.requireNonNull(exception.getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<APIResponse> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new APIResponse("Access denied", null));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    ResponseEntity<APIResponse> handleBadCredentialsException(BadCredentialsException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse("Unauthorized request", null));
    }

    @ExceptionHandler(value = AccountStatusException.class)
    ResponseEntity<APIResponse> handleAccountStatusException(AccountStatusException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new APIResponse("User is locked", null));
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    ResponseEntity<APIResponse> handleExpiredJwtException(ExpiredJwtException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse("Unauthorized request", null));
    }

    @ExceptionHandler(value = SignatureException.class)
    ResponseEntity<APIResponse> handleSignatureException(SignatureException exception) {
        log.error("Exception: {}", String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse("Unauthorized request", null));
    }

}
