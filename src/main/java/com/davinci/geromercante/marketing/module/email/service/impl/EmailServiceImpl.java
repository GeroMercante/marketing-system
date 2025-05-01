package com.davinci.geromercante.marketing.module.email.service.impl;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.module.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String ENCODING = "UTF-8";
    private final JavaMailSender javaMailSender;
    private final MessageUtil messageUtil;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendTemporaryPassword(String destinatary, String temporaryPassword) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        Context context = new Context();
        context.setVariables(Map.of(
                "nombre", destinatary,
                "password", temporaryPassword
        ));

        String htmlContent = templateEngine.process("email-password", context);

        helper.setTo(destinatary);
        helper.setSubject(messageUtil.getMessage("email.password.temporary"));
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    @Override
    public void sendVerificationCode(String destination, String verificationCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        Context context = new Context();
        context.setVariables(Map.of(
                "nombre", destination,
                "codigo", verificationCode
        ));

        String htmlContent = templateEngine.process("email-verification", context);

        helper.setTo(destination);
        helper.setSubject(messageUtil.getMessage("email.code.verification"));
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    @Override
    public void sendRecoveryAccount(String destinatary, String verificationCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        Context context = new Context();
        context.setVariables(Map.of(
                "nombre", destinatary,
                "codigo", verificationCode
        ));

        String htmlContent = templateEngine.process("email-recovery", context);

        helper.setTo(destinatary);
        helper.setSubject(messageUtil.getMessage("email.code.recovery.password"));
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}
