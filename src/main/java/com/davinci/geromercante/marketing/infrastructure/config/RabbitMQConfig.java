package com.davinci.geromercante.marketing.infrastructure.config;

import com.davinci.geromercante.marketing.infrastructure.config.constants.RabbitMQConstants;
import com.davinci.geromercante.marketing.infrastructure.exception.RabbitMQErrorHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyTimeout(RabbitMQConstants.ACK_TIMEOUT);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, RabbitMQErrorHandler rabbitMQErrorHandler) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(true);
        factory.setErrorHandler(rabbitMQErrorHandler);
        return factory;
    }

    // EXCHANGES
    @Bean
    public TopicExchange emailTemporaryPasswordExchange() {
        return new TopicExchange(RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_EXCHANGE);
    }

    @Bean
    public TopicExchange emailVerificationCodeExchange() {
        return new TopicExchange(RabbitMQConstants.EMAIL_VERIFICATION_CODE_EXCHANGE);
    }

    @Bean
    public TopicExchange emailRecoveryAccountExchange() {
        return new TopicExchange(RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_EXCHANGE);
    }

    // QUEUES
    @Bean
    public Queue emailTemporaryPasswordQueue() {
        return new Queue(RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_QUEUE, true);
    }

    @Bean
    public Queue emailVerificationCodeQueue() {
        return new Queue(RabbitMQConstants.EMAIL_VERIFICATION_CODE_QUEUE, true);
    }

    @Bean
    public Queue emailRecoveryAccountQueue() {
        return new Queue(RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_QUEUE, true);
    }

    // ROUTING
    @Bean
    public Binding bindingTemporaryPassword(Queue emailTemporaryPasswordQueue, TopicExchange emailTemporaryPasswordExchange) {
        return BindingBuilder
                .bind(emailTemporaryPasswordQueue)
                .to(emailTemporaryPasswordExchange)
                .with(RabbitMQConstants.EMAIL_TEMPORARY_PASSWORD_ROUTING_KEY);
    }

    @Bean
    public Binding bindingVerificationCode(Queue emailVerificationCodeQueue, TopicExchange emailVerificationCodeExchange) {
        return BindingBuilder
                .bind(emailVerificationCodeQueue)
                .to(emailVerificationCodeExchange)
                .with(RabbitMQConstants.EMAIL_VERIFICATION_CODE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingRecoveryAccount(Queue emailRecoveryAccountQueue, TopicExchange emailRecoveryAccountExchange) {
        return BindingBuilder
                .bind(emailRecoveryAccountQueue)
                .to(emailRecoveryAccountExchange)
                .with(RabbitMQConstants.EMAIL_RECOVERY_ACCOUNT_ROUTING_KEY);
    }

}
