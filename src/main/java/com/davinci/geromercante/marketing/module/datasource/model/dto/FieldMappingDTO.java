package com.davinci.geromercante.marketing.module.datasource.model.dto;

import com.davinci.geromercante.marketing.common.model.dto.BaseDTO;
import com.davinci.geromercante.marketing.module.datasource.model.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldMappingDTO extends BaseDTO {
    private String sourceField;
    private String targetField;
    private EntityType entityType;
    private String description;
    private Boolean isRequired;
    private String defaultValue;
    private String transformationExpression;
} 
