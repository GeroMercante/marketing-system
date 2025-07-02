package com.davinci.geromercante.marketing.module.datasource.model.mapper;

import com.davinci.geromercante.marketing.module.datasource.model.dto.response.DataSourceResponseDTO;
import com.davinci.geromercante.marketing.module.datasource.model.entity.DataSource;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class DataSourceToResponseDtoConverter implements Converter<DataSource, DataSourceResponseDTO> {

    @Override
    public DataSourceResponseDTO convert(MappingContext<DataSource, DataSourceResponseDTO> context) {
        DataSource entity = context.getSource();
        DataSourceResponseDTO dto = context.getDestination() == null ? new DataSourceResponseDTO() : context.getDestination();

        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUrl(entity.getUrl());
        dto.setFormat(entity.getFormat());
        dto.setAuthenticationType(entity.getAuthenticationType());
        dto.setActive(entity.getActive());
        dto.setTimeoutSeconds(entity.getTimeoutSeconds());
        dto.setRetryAttempts(entity.getRetryAttempts());

        // Mapear field mappings si existen
        if (entity.getFieldMappings() != null && !entity.getFieldMappings().isEmpty()) {
            dto.setFieldMappings(entity.getFieldMappings().stream()
                    .map(fieldMapping -> {
                        com.davinci.geromercante.marketing.module.datasource.model.dto.FieldMappingDTO fieldMappingDTO = 
                                new com.davinci.geromercante.marketing.module.datasource.model.dto.FieldMappingDTO();
                        fieldMappingDTO.setId(fieldMapping.getId());
                        fieldMappingDTO.setCreatedAt(fieldMapping.getCreatedAt());
                        fieldMappingDTO.setUpdatedAt(fieldMapping.getUpdatedAt());
                        fieldMappingDTO.setSourceField(fieldMapping.getSourceField());
                        fieldMappingDTO.setTargetField(fieldMapping.getTargetField());
                        fieldMappingDTO.setEntityType(fieldMapping.getEntityType());
                        fieldMappingDTO.setDescription(fieldMapping.getDescription());
                        fieldMappingDTO.setIsRequired(fieldMapping.getIsRequired());
                        fieldMappingDTO.setDefaultValue(fieldMapping.getDefaultValue());
                        fieldMappingDTO.setTransformationExpression(fieldMapping.getTransformationExpression());
                        return fieldMappingDTO;
                    })
                    .toList());
        }

        return dto;
    }
} 