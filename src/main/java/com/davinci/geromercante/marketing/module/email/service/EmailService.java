package com.davinci.geromercante.marketing.module.email.service;

import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendTemporaryPassword(String destinatary, String temporaryPassword) throws MarketingException, MessagingException;
    void sendVerificationCode(String destinatary, String verificationCode) throws MarketingException, MessagingException;
    void sendRecoveryAccount(String destinatary, String verificationCode) throws MarketingException, MessagingException;
}
