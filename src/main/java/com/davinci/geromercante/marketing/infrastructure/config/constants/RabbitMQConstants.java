package com.davinci.geromercante.marketing.infrastructure.config.constants;

public final class RabbitMQConstants {

    private RabbitMQConstants() {}

    // Exchanges
    public static final String EMAIL_TEMPORARY_PASSWORD_EXCHANGE = "email.temporary.password.exchange";
    public static final String EMAIL_VERIFICATION_CODE_EXCHANGE = "email.verification.code.exchange";
    public static final String EMAIL_RECOVERY_ACCOUNT_EXCHANGE = "email.recovery.account.exchange";

    // Queues
    public static final String EMAIL_TEMPORARY_PASSWORD_QUEUE = "email.temporary.password.queue";
    public static final String EMAIL_VERIFICATION_CODE_QUEUE = "email.verification.code.queue";
    public static final String EMAIL_RECOVERY_ACCOUNT_QUEUE = "email.recovery.account.queue";

    // Routing Keys
    public static final String EMAIL_TEMPORARY_PASSWORD_ROUTING_KEY = "email.temporary.password";
    public static final String EMAIL_VERIFICATION_CODE_ROUTING_KEY = "email.verification.code";
    public static final String EMAIL_RECOVERY_ACCOUNT_ROUTING_KEY = "email.recovery.account";

    public static final long ACK_TIMEOUT = 500L;
}
