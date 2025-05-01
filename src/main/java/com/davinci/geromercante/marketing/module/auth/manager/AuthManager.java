package com.davinci.geromercante.marketing.module.auth.manager;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.ChangePasswordRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.EmailVerificationRequest;
import com.davinci.geromercante.marketing.module.auth.model.dto.request.LoginRequest;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.entity.EmailVerification;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialStatusEnum;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.repository.CredentialRepository;
import com.davinci.geromercante.marketing.module.auth.repository.EmailVerificationRepository;
import com.davinci.geromercante.marketing.module.email.messaging.EmailProducer;
import com.davinci.geromercante.marketing.module.user.manager.UserManager;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthManager {

    @Value("${code.verification.expiration}")
    private long codeVerificationExpiration;

    private static final String CREDENTIAL_NOT_FOUND_EX = "credential.not.found";

    private final MessageUtil messageUtil;
    private final AuthenticationManager authenticationManager;
    private final CredentialManager credentialManager;
    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final JwtUtil jwtUtil;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailProducer emailProducer;

    public Credential authenticateWithEmailPassword(LoginRequest loginRequest) throws MarketingException {
        User user = userManager.findByEmail(loginRequest.getEmail());
        Credential credential = credentialManager.getByUserAndTypeCredential(user, CredentialTypeEnum.INTERNAL);

        if (credential == null) {
            throw new MarketingException(messageUtil.getMessage(CREDENTIAL_NOT_FOUND_EX));
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        return credential;
    }

    public Credential changePassword(ChangePasswordRequest changeTemporaryPasswordDTO, String token) throws MarketingException {
        String userEmail = jwtUtil.extractUsername(token);
        Credential credential = credentialRepository.findByUserEmailAndTypeCredential(userEmail, CredentialTypeEnum.INTERNAL).orElseThrow(() -> new MarketingException(messageUtil.getMessage(CREDENTIAL_NOT_FOUND_EX)));

        if (!passwordEncoder.matches(changeTemporaryPasswordDTO.getOldPassword(), credential.getPassword())) {
            throw new MarketingException(messageUtil.getMessage("password.not.match"));
        }

        credential.setIsTemporary(false);
        credential.setPassword(passwordEncoder.encode(changeTemporaryPasswordDTO.getNewPassword()));
        return credentialRepository.save(credential);
    }

    public void registerUserApp(String email, String hashedPassword, String verificationCode) throws MarketingException {
        if (!credentialRepository.existsByUser_EmailAndTypeCredential(email, CredentialTypeEnum.INTERNAL)) {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.setEmail(email);
            emailVerification.setPassword(hashedPassword);
            emailVerification.setVerificationCode(verificationCode);
            emailVerification.setStatus(CredentialStatusEnum.PENDING.name());

            emailVerificationRepository.save(emailVerification);
            emailProducer.sendVerificationCodeEmail(email, verificationCode);
        } else {
            throw new MarketingException(messageUtil.getMessage("email.internal.credential.exists"));
        }
    }

    public EmailVerification verifyUserApp(EmailVerificationRequest emailVerificationDTO) throws MarketingException {
        EmailVerification emailVerification = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(emailVerificationDTO.getEmail())
                .orElseThrow(() -> new MarketingException(messageUtil.getMessage(CREDENTIAL_NOT_FOUND_EX)));

        Instant now = Instant.now();
        Instant expirationTime = emailVerification.getCreatedAt().toInstant().plusMillis(codeVerificationExpiration);

        if (expirationTime.isBefore(now)) {
            emailVerification.setStatus(CredentialStatusEnum.INVALID.name());
            emailVerificationRepository.save(emailVerification);
            throw new MarketingException(HttpStatus.REQUEST_TIMEOUT, messageUtil.getMessage("error.code.expired"), ErrorCodeResponse.EXPIRE_CODE);
        }

        if (emailVerificationDTO.getVerificationCode().equals(emailVerification.getVerificationCode())) {
            if (emailVerification.getStatus().equals(CredentialStatusEnum.PENDING.name())) {
                emailVerification.setStatus(CredentialStatusEnum.VERIFIED.name());
                return emailVerificationRepository.save(emailVerification);
            } else {
                throw new MarketingException(messageUtil.getMessage("email.already_verified"));
            }
        } else {
            throw new MarketingException(messageUtil.getMessage("email.code_not_match"));
        }
    }

    public void saveSession(Credential credential, String jwt) {
        sessionManager.saveSession(credential, credential.getUser(), credential.getUser().getProfile(), jwt);
    }
}
