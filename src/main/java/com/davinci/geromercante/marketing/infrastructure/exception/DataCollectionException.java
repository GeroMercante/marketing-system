package com.davinci.geromercante.marketing.infrastructure.exception;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;

public class DataCollectionException extends RuntimeException {

    private final ErrorCodeResponse errorCode;

    public DataCollectionException(String message, ErrorCodeResponse errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataCollectionException(String message, Throwable cause, ErrorCodeResponse errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeResponse getErrorCode() {
        return errorCode;
    }
} 