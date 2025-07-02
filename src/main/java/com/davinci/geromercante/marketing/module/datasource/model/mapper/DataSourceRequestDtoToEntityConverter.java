package com.davinci.geromercante.marketing.module.datasource.model.mapper;

import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.model.entity.DataSource;
import com.davinci.geromercante.marketing.module.datasource.model.entity.FieldMapping;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataSourceRequestDtoToEntityConverter implements Converter<DataSourceRequestDTO, DataSource> {

    @Override
    public DataSource convert(MappingContext<DataSourceRequestDTO, DataSource> context) {
        DataSourceRequestDTO dto = context.getSource();
        DataSource entity = context.getDestination() == null ? new DataSource() : context.getDestination();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUrl(dto.getUrl());
        entity.setFormat(dto.getFormat());
        entity.setAuthenticationType(dto.getAuthenticationType());
        entity.setApiKey(dto.getApiKey());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setBearerToken(dto.getBearerToken());
        entity.setActive(dto.getActive());
        entity.setTimeoutSeconds(dto.getTimeoutSeconds());
        entity.setRetryAttempts(dto.getRetryAttempts());

        if (dto.getFieldMappings() != null) {
            entity.getFieldMappings().clear();
            dto.getFieldMappings().forEach(fieldMappingDTO -> {
                FieldMapping fieldMapping = new FieldMapping();
                fieldMapping.setSourceField(fieldMappingDTO.getSourceField());
                fieldMapping.setTargetField(fieldMappingDTO.getTargetField());
                fieldMapping.setEntityType(fieldMappingDTO.getEntityType());
                fieldMapping.setDescription(fieldMappingDTO.getDescription());
                fieldMapping.setIsRequired(fieldMappingDTO.getIsRequired());
                fieldMapping.setDefaultValue(fieldMappingDTO.getDefaultValue());
                fieldMapping.setTransformationExpression(fieldMappingDTO.getTransformationExpression());
                fieldMapping.setDataSource(entity);
                entity.getFieldMappings().add(fieldMapping);
            });
        } else {
            entity.setFieldMappings(new ArrayList<>());
        }

        return entity;
    }
} 