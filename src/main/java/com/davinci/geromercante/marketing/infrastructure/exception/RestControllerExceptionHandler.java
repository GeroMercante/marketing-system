package com.davinci.geromercante.marketing.infrastructure.exception;

import com.davinci.geromercante.marketing.common.model.dto.ApiResponseErrorDTO;
import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    private final MessageUtil messageUtil;

    public RestControllerExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponseErrorDTO> handleExpireJwt() {
        ApiResponseErrorDTO apiResponse = new ApiResponseErrorDTO();
        apiResponse.setCode(ErrorCodeResponse.EXPIRE_CODE.getCode());
        apiResponse.setStatus(HttpStatus.UNAUTHORIZED);
        apiResponse.setMessage(messageUtil.getMessage("exception.jwt.expired"));

        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseErrorDTO> handleException(Exception e) {
        if (e instanceof MarketingException apiException) {
            ApiResponseErrorDTO apiResponse = new ApiResponseErrorDTO();
            apiResponse.setCode(apiException.getCode().getCode());
            apiResponse.setStatus(apiException.getStatus());
            apiResponse.setMessage(apiException.getMessage());

            return new ResponseEntity<>(apiResponse, apiException.getStatus());
        } else if (e instanceof DataCollectionException dataCollectionException) {
            log.error("Error en recolección de datos: {}", dataCollectionException.getMessage());
            ApiResponseErrorDTO apiResponse = new ApiResponseErrorDTO();
            apiResponse.setCode(dataCollectionException.getErrorCode().getCode());
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setMessage(dataCollectionException.getMessage());

            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        } else {
            log.error("Excepción no contemplada: {}", e.getMessage());
            ApiResponseErrorDTO apiResponse = new ApiResponseErrorDTO();
            apiResponse.setCode(ErrorCodeResponse.UNHANDLE.getCode());
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            apiResponse.setMessage(messageUtil.getMessage("unhandle-error"));

            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }
}
