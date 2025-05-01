package com.davinci.geromercante.marketing.module.email.messaging;

import com.davinci.geromercante.marketing.infrastructure.config.constants.RabbitMQConstants;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.EmailResponse;
import com.davinci.geromercante.marketing.module.email.service.EmailService;
import com.rabbitmq.client.Channel;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_QUEUE)
    public void processTemporaryPassword(EmailResponse message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws MarketingException, MessagingException, IOException {
        try {
            emailService.sendTemporaryPassword(message.getRecipient(), message.getMessage());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("ERROR processTemporaryPassword - Exception: {}", e.getMessage());
            channel.basicNack(tag, false, true);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConstants.EMAIL_VERIFICATION_CODE_QUEUE)
    public void processVerificationCode(EmailResponse message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws MarketingException, MessagingException, IOException {
        try {
            emailService.sendVerificationCode(message.getRecipient(), message.getMessage());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("ERROR processVerificationCode - Exception: {}", e.getMessage());
            channel.basicNack(tag, false, false);
            throw e;
        }
    }

    @RabbitListener(queues = RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_QUEUE)
    public void processRecoveryAccount(EmailResponse message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws MarketingException, MessagingException, IOException {
        try {
            emailService.sendRecoveryAccount(message.getRecipient(), message.getMessage());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("ERROR processRecoveryAccount - Exception: {}", e.getMessage());
            channel.basicNack(tag, false, false);
            throw e;
        }
    }
}
