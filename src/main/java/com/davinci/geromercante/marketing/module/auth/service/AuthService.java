package com.davinci.geromercante.marketing.module.auth.service;

import com.davinci.geromercante.marketing.common.model.dto.GenericMessage;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.ChangePasswordRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.EmailVerificationRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.LoginRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest credentialDTO) throws MarketingException;
    GenericMessage registerUserApp(LoginRequest registrationRequestDTO) throws MarketingException;
    LoginResponse verifyUserApp(EmailVerificationRequest emailVerificationDTO) throws MarketingException;
    LoginResponse changePassword(ChangePasswordRequest changeTemporaryPasswordDTO, String token) throws MarketingException;
}
