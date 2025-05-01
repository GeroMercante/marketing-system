package com.davinci.geromercante.marketing.infrastructure.aop;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.annotation.RequiresPermission;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class PermissionAspect {

    private final MessageUtil messageUtil;

    public PermissionAspect(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        Set<String> requiredPermissions = Arrays.stream(requiresPermission.value())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> userPermissions = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (userPermissions.stream().anyMatch(requiredPermissions::contains)) {
            return joinPoint.proceed();
        } else {
            throw new MarketingException(HttpStatus.UNAUTHORIZED, messageUtil.getMessage("exception.unauthorized"), ErrorCodeResponse.UNAUTHORIZED);
        }
    }
}
