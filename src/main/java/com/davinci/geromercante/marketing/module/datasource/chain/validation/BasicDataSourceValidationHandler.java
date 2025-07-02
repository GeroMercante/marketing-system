package com.davinci.geromercante.marketing.module.datasource.chain.validation;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.chain.AbstractDataSourceValidationHandler;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@RequiredArgsConstructor
public class BasicDataSourceValidationHandler extends AbstractDataSourceValidationHandler {

    private final MessageUtil messageUtil;

    @Override
    protected boolean canHandle(DataSourceRequestDTO dataSource) {
        return true;
    }

    @Override
    protected void doValidate(DataSourceRequestDTO dataSource) throws DataCollectionException {
        validateRequiredFields(dataSource);
        validateUrl(dataSource.getUrl());
        validateTimeoutAndRetries(dataSource);
    }

    private void validateRequiredFields(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (dataSource.getName() == null || dataSource.getName().trim().isEmpty()) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.name.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getUrl() == null || dataSource.getUrl().trim().isEmpty()) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.url.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getFormat() == null) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.format.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getAuthenticationType() == null) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.authentication.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }

    private void validateUrl(String url) throws DataCollectionException {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                throw new DataCollectionException(
                        messageUtil.getMessage("datasource.url.invalid.protocol"), 
                        ErrorCodeResponse.BUSINESS_ERROR
                );
            }
            uri.toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.url.invalid"),
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
    

    private void validateTimeoutAndRetries(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (dataSource.getTimeoutSeconds() != null && dataSource.getTimeoutSeconds() <= 0) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.timeout.invalid"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getRetryAttempts() != null && dataSource.getRetryAttempts() < 0) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.retry.invalid"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
} 