package com.davinci.geromercante.marketing.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@Slf4j
public class RabbitMQErrorHandler implements ErrorHandler {

    @Override
    public void handleError(@NonNull Throwable t) {
        if (t instanceof ListenerExecutionFailedException) {
            log.info("========================================================");
            log.info("Error en RabbitMQ: {}", t.getMessage());
            log.info("========================================================");
        }
    }
}
