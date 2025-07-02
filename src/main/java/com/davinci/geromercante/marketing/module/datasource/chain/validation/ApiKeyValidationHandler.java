package com.davinci.geromercante.marketing.module.datasource.chain.validation;

import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.chain.AbstractDataSourceValidationHandler;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.model.enums.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyValidationHandler extends AbstractDataSourceValidationHandler {

    private final MessageUtil messageUtil;

    @Override
    protected boolean canHandle(DataSourceRequestDTO dataSource) {
        return AuthenticationType.API_KEY.equals(dataSource.getAuthenticationType());
    }

    @Override
    protected void doValidate(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (dataSource.getApiKey() == null || dataSource.getApiKey().trim().isEmpty()) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.apikey.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getApiKey().length() < 10) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.apikey.too.short"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
} 