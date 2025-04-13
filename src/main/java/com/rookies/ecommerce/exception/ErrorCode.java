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
    RESOURCE_NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    INVALID_IMAGE_FILE("Invalid image file", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    String message;

    HttpStatusCode httpStatusCode;

}
