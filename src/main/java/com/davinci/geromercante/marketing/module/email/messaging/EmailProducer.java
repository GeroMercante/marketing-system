package com.davinci.geromercante.marketing.module.email.messaging;

import com.davinci.geromercante.marketing.infrastructure.config.constants.RabbitMQConstants;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.EmailResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTemporaryPasswordEmail(String destinatary, String temporaryPassword) {
        EmailResponse message = new EmailResponse(destinatary, temporaryPassword);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_EXCHANGE,
                                      RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_ROUTING_KEY,
                                      message);
    }

    public void sendVerificationCodeEmail(String destinatary, String verificationCode) {
        EmailResponse message = new EmailResponse(destinatary, verificationCode);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EMAIL_VERIFICATION_CODE_EXCHANGE,
                                      RabbitMQConstants.EMAIL_VERIFICATION_CODE_ROUTING_KEY,
                                      message);
    }

    public void sendRecoveryAccountEmail(String destinatary, String verificationCode) {
        EmailResponse message = new EmailResponse(destinatary, verificationCode);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_EXCHANGE,
                                      RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_ROUTING_KEY,
                                      message);
    }
}
