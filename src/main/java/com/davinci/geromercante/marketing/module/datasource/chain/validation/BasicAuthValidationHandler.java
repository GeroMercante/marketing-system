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
public class BasicAuthValidationHandler extends AbstractDataSourceValidationHandler {

    private final MessageUtil messageUtil;

    @Override
    protected boolean canHandle(DataSourceRequestDTO dataSource) {
        return AuthenticationType.BASIC_AUTH.equals(dataSource.getAuthenticationType());
    }

    @Override
    protected void doValidate(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (dataSource.getUsername() == null || dataSource.getUsername().trim().isEmpty()) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.username.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getPassword() == null || dataSource.getPassword().trim().isEmpty()) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.password.required"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getUsername().length() < 3) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.username.too.short"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        if (dataSource.getPassword().length() < 6) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.password.too.short"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
} 