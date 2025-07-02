package com.davinci.geromercante.marketing.module.datasource.manager;

import com.davinci.geromercante.marketing.common.util.EntitySortUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.module.datasource.model.entity.DataSource;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.repository.DataSourceRepository;
import com.davinci.geromercante.marketing.module.datasource.chain.DataSourceValidationChainFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataSourceManager {

    private final DataSourceRepository dataSourceRepository;
    private final MessageUtil messageUtil;
    private final DataSourceValidationChainFactory validationChainFactory;
    private final ModelMapper modelMapper;

    public Page<DataSource> search(int page, int size, String searchText, String orderBy, String orderDirection) {
        Sort sort = EntitySortUtil.getSortForEntity(DataSource.class, orderBy, orderDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        return dataSourceRepository.search(searchText, pageable);
    }

    public DataSource getDataSource(Long id) throws DataCollectionException {
        return dataSourceRepository.findById(id)
                .orElseThrow(() -> new DataCollectionException(
                        messageUtil.getMessage("datasource.not.found"), 
                        ErrorCodeResponse.BUSINESS_ERROR
                ));
    }

    public DataSource createDataSource(DataSourceRequestDTO dataSourceDto) throws DataCollectionException {
        validationChainFactory.createValidationChain().validate(dataSourceDto);
        DataSource dataSource = modelMapper.map(dataSourceDto, DataSource.class);
        checkUniqueConstraints(dataSource, null);
        
        return dataSourceRepository.save(dataSource);
    }

    public DataSource updateDataSource(Long id, DataSourceRequestDTO dataSourceDto) throws DataCollectionException {
        DataSource existingDataSource = getDataSource(id);
        validationChainFactory.createValidationChain().validate(dataSourceDto);
        DataSource dataSource = modelMapper.map(dataSourceDto, DataSource.class);
        checkUniqueConstraints(dataSource, id);
        dataSource.setId(existingDataSource.getId());
        dataSource.setCreatedAt(existingDataSource.getCreatedAt());
        dataSource.setCreatedBy(existingDataSource.getCreatedBy());
        
        return dataSourceRepository.save(dataSource);
    }

    public void deleteDataSource(Long id) throws DataCollectionException {
        DataSource dataSource = getDataSource(id);
        dataSource.setDeleted(Boolean.TRUE);
        dataSourceRepository.save(dataSource);
    }

    public List<DataSource> findActiveDataSources() {
        return dataSourceRepository.findByActiveAndDeleted(Boolean.TRUE, Boolean.FALSE);
    }

    private void checkUniqueConstraints(DataSource dataSource, Long excludeId) throws DataCollectionException {
        // Verificar nombre único
        Optional<DataSource> existingByName = dataSourceRepository.findByNameAndDeleted(dataSource.getName(), Boolean.FALSE);
        if (existingByName.isPresent() && (excludeId == null || !existingByName.get().getId().equals(excludeId))) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.name.exists"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }

        // Verificar URL única
        Optional<DataSource> existingByUrl = dataSourceRepository.findByUrlAndDeleted(dataSource.getUrl(), Boolean.FALSE);
        if (existingByUrl.isPresent() && (excludeId == null || !existingByUrl.get().getId().equals(excludeId))) {
            throw new DataCollectionException(
                    messageUtil.getMessage("datasource.url.exists"), 
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
} 