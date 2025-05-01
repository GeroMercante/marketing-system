package com.davinci.geromercante.marketing.infrastructure.aop;

import com.davinci.geromercante.marketing.infrastructure.annotation.MonitoringMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MonitoringAspect {

    private static final String LOG_MESSAGE = "Método {} ejecutado en {} ms";

    @Around("@annotation(monitoringMethod)")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint, MonitoringMethod monitoringMethod) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        switch (monitoringMethod.level()) {
            case DEBUG:
                log.debug(LOG_MESSAGE, joinPoint.getSignature(), (endTime - startTime));
                break;
            case ERROR:
                log.error(LOG_MESSAGE, joinPoint.getSignature(), (endTime - startTime));
                break;
            case WARN:
                log.warn(LOG_MESSAGE, joinPoint.getSignature(), (endTime - startTime));
                break;
            case TRACE:
                log.trace(LOG_MESSAGE, joinPoint.getSignature(), (endTime - startTime));
                break;
            case INFO:
            default:
                log.info(LOG_MESSAGE, joinPoint.getSignature(), (endTime - startTime));
                break;
        }
        return result;
    }
}
