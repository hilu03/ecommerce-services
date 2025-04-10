package com.rookies.ecommerce.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNKNOWN_ERROR("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),

    ;

    ErrorCode(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    String message;

    HttpStatusCode httpStatusCode;

}
