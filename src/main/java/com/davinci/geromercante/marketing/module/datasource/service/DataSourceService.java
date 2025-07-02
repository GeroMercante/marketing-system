package com.davinci.geromercante.marketing.module.datasource.service;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.model.dto.response.DataSourceResponseDTO;

public interface DataSourceService {
    PagedResponse<DataSourceResponseDTO> search(int page, int size, String searchText, String orderBy, String orderDirection);
    DataSourceResponseDTO getDataSource(Long id) throws DataCollectionException;
    DataSourceResponseDTO createDataSource(DataSourceRequestDTO request) throws DataCollectionException;
    DataSourceResponseDTO updateDataSource(Long id, DataSourceRequestDTO request) throws DataCollectionException;
    void deleteDataSource(Long id) throws DataCollectionException;
} 
