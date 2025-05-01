package com.davinci.geromercante.marketing.module.auth.controller;

import com.davinci.geromercante.marketing.common.model.dto.GenericMessage;
import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.infrastructure.validation.DomainValidator;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.ChangePasswordRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.EmailVerificationRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.LoginRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.LoginResponse;
import com.davinci.geromercante.marketing.module.auth.service.AuthService;
import com.davinci.geromercante.marketing.module.auth.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstant.CONTEXT_PATH + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final SessionService sessionService;
    private final DomainValidator domainValidator;
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws MarketingException {
        domainValidator.validateEmail(loginRequest.getEmail());
        return this.authService.login(loginRequest);
    }

    @PostMapping("/register")
    public GenericMessage registerUserApp(@RequestBody LoginRequest registrationRequestDTO) throws MarketingException {
        domainValidator.validateEmail(registrationRequestDTO.getEmail());
        return authService.registerUserApp(registrationRequestDTO);
    }

    @PostMapping("/verify")
    public LoginResponse verifyUserApp(@RequestBody EmailVerificationRequest emailVerificationDTO) throws MarketingException {
        domainValidator.validateEmail(emailVerificationDTO.getEmail());
        return authService.verifyUserApp(emailVerificationDTO);
    }

    @PutMapping("/change/password")
    public LoginResponse changePassword(
            @RequestBody ChangePasswordRequest changeTemporaryPasswordDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        final String jwtToken = jwtUtil.getToken(authHeader);
        return authService.changePassword(changeTemporaryPasswordDTO, jwtToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        final String jwtToken = jwtUtil.getToken(authHeader);
        sessionService.revokeToken(jwtToken);
    }
}
