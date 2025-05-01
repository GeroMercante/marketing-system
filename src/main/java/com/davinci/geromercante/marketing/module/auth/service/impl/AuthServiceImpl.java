package com.davinci.geromercante.marketing.module.auth.service.impl;

import com.davinci.geromercante.marketing.common.model.dto.GenericMessage;
import com.davinci.geromercante.marketing.common.service.BaseService;
import com.davinci.geromercante.marketing.common.util.GeneratorUtil;
import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.manager.AuthManager;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.ChangePasswordRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.EmailVerificationRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.LoginRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.CredentialResponse;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.LoginResponse;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.entity.EmailVerification;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.service.AuthService;
import com.davinci.geromercante.marketing.module.user.manager.UserManager;
import com.davinci.geromercante.marketing.module.user.model.dto.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends BaseService implements AuthService {

    @Value("${jwt.expiration.time}")
    private long expirationTime;
    private final PasswordEncoder passwordEncoder;
    private final UserManager userManager;
    private final AuthManager authManager;
    private final MessageUtil messageUtil;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) throws MarketingException {
        Credential credential = authManager.authenticateWithEmailPassword(loginRequest);
        return getLoginResponseDTO(credential);
    }

    @Override
    @Transactional
    public GenericMessage registerUserApp(LoginRequest registrationRequestDTO) throws MarketingException {
        String email = registrationRequestDTO.getEmail();
        String hashedPassword = passwordEncoder.encode(registrationRequestDTO.getPassword());
        String verificationCode = GeneratorUtil.generateVerificationCode();

        authManager.registerUserApp(email, hashedPassword, verificationCode);

        return new GenericMessage(messageUtil.getMessage("feedback.user.register"));
    }

    @Override
    @Transactional
    public LoginResponse verifyUserApp(EmailVerificationRequest emailVerificationDTO) throws MarketingException {
        EmailVerification emailVerification = authManager.verifyUserApp(emailVerificationDTO);

        UserResponse dto = new UserResponse();
        dto.setEmail(emailVerification.getEmail());
        dto.setActive(true);

        CredentialResponse credentialDTO = new CredentialResponse();
        credentialDTO.setPassword(emailVerification.getPassword());
        credentialDTO.setTypeCredential(CredentialTypeEnum.INTERNAL);
        credentialDTO.setIsTemporary(false);
        credentialDTO.setIsVerified(true);

        userManager.createUser(dto, credentialDTO);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(emailVerification.getEmail());
        loginRequest.setPassword(emailVerificationDTO.getPassword());
        return login(loginRequest);
    }

    @Override
    @Transactional
    public LoginResponse changePassword(ChangePasswordRequest changeTemporaryPasswordDTO, String token) throws MarketingException {
        Credential credential = authManager.changePassword(changeTemporaryPasswordDTO, token);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(credential.getUser().getEmail());
        loginRequest.setPassword(changeTemporaryPasswordDTO.getNewPassword());
        return login(loginRequest);
    }

    private LoginResponse getLoginResponseDTO(Credential credential) {
        String jwt = jwtUtil.generateToken(credential);

        LoginResponse loginResponse = this.convertToDto(credential, LoginResponse.class);
        loginResponse.setToken(jwt);
        loginResponse.setExpirationTime(expirationTime);

        authManager.saveSession(credential, jwt);

        return loginResponse;
    }
}
