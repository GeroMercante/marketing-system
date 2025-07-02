package com.davinci.geromercante.marketing.module.datasource.chain;

import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;

public interface DataSourceValidationHandler {
    void setNext(DataSourceValidationHandler next);
    void validate(DataSourceRequestDTO dataSource) throws DataCollectionException;
} 
