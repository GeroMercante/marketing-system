package com.davinci.geromercante.marketing.module.datasource.service.impl;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.common.service.BaseService;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.datasource.manager.DataSourceManager;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.model.dto.response.DataSourceResponseDTO;
import com.davinci.geromercante.marketing.module.datasource.model.entity.DataSource;
import com.davinci.geromercante.marketing.module.datasource.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl extends BaseService implements DataSourceService {

    private final DataSourceManager dataSourceManager;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DataSourceResponseDTO> search(int page, int size, String searchText, String orderBy, String orderDirection) {
        Page<DataSource> dataSourcePage = dataSourceManager.search(page, size, searchText, orderBy, orderDirection);
        return PagedResponse.from(this.convertToDto(dataSourcePage.getContent(), DataSourceResponseDTO.class), dataSourcePage);
    }

    @Override
    @Transactional(readOnly = true)
    public DataSourceResponseDTO getDataSource(Long id) throws DataCollectionException {
        DataSource dataSource = dataSourceManager.getDataSource(id);
        return this.convertToDto(dataSource, DataSourceResponseDTO.class);
    }

    @Override
    @Transactional
    public DataSourceResponseDTO createDataSource(DataSourceRequestDTO request) throws DataCollectionException {
        DataSource savedDataSource = dataSourceManager.createDataSource(request);
        return this.convertToDto(savedDataSource, DataSourceResponseDTO.class);
    }

    @Override
    @Transactional
    public DataSourceResponseDTO updateDataSource(Long id, DataSourceRequestDTO request) throws DataCollectionException {
        DataSource updatedDataSource = dataSourceManager.updateDataSource(id, request);
        return this.convertToDto(updatedDataSource, DataSourceResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteDataSource(Long id) throws DataCollectionException {
        dataSourceManager.deleteDataSource(id);
    }
} 