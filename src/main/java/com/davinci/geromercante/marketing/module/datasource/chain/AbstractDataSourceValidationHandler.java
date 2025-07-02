package com.davinci.geromercante.marketing.module.datasource.chain;

import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;

public abstract class AbstractDataSourceValidationHandler implements DataSourceValidationHandler {

    private DataSourceValidationHandler next;

    @Override
    public void setNext(DataSourceValidationHandler next) {
        this.next = next;
    }

    @Override
    public void validate(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (canHandle(dataSource)) {
            doValidate(dataSource);
        }
        
        if (next != null) {
            next.validate(dataSource);
        }
    }

    protected abstract boolean canHandle(DataSourceRequestDTO dataSource);
    protected abstract void doValidate(DataSourceRequestDTO dataSource) throws DataCollectionException;
} 