package com.davinci.geromercante.marketing.module.datasource.chain;

import com.davinci.geromercante.marketing.module.datasource.chain.validation.ApiKeyValidationHandler;
import com.davinci.geromercante.marketing.module.datasource.chain.validation.BasicAuthValidationHandler;
import com.davinci.geromercante.marketing.module.datasource.chain.validation.BasicDataSourceValidationHandler;
import com.davinci.geromercante.marketing.module.datasource.chain.validation.BearerTokenValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSourceValidationChainFactory {

    private final BasicDataSourceValidationHandler basicValidationHandler;
    private final ApiKeyValidationHandler apiKeyValidationHandler;
    private final BearerTokenValidationHandler bearerTokenValidationHandler;
    private final BasicAuthValidationHandler basicAuthValidationHandler;

    public DataSourceValidationHandler createValidationChain() {
        // Configurar el chain: básico -> api key -> bearer token -> basic auth
        basicValidationHandler.setNext(apiKeyValidationHandler);
        apiKeyValidationHandler.setNext(bearerTokenValidationHandler);
        bearerTokenValidationHandler.setNext(basicAuthValidationHandler);
        
        return basicValidationHandler;
    }
} 