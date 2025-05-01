package com.davinci.geromercante.marketing.common.model.enums;

import lombok.Getter;

@Getter
public enum ErrorCodeResponse {
    UNHANDLE(-1),
    BUSINESS_ERROR(1),
    UNAUTHORIZED(2),
    INVALID_TOKEN(3),
    EXPIRE_CODE(4);

    private final int code;

    ErrorCodeResponse(int code) {
        this.code = code;
    }
}
