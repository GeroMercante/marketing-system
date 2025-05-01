package com.davinci.geromercante.marketing.infrastructure.exception;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MarketingException extends Exception {

    private final HttpStatus status;
    private final ErrorCodeResponse code;

    public MarketingException(HttpStatus status, String message, ErrorCodeResponse code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public MarketingException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = ErrorCodeResponse.BUSINESS_ERROR;
    }

}
