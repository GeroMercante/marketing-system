package com.davinci.geromercante.marketing.module.datasource.model.dto.response;

import com.davinci.geromercante.marketing.common.model.dto.BaseDTO;
import com.davinci.geromercante.marketing.module.datasource.model.dto.FieldMappingDTO;
import com.davinci.geromercante.marketing.module.datasource.model.enums.AuthenticationType;
import com.davinci.geromercante.marketing.module.datasource.model.enums.ResponseFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceResponseDTO extends BaseDTO {
    private String name;
    private String description;
    private String url;
    private ResponseFormat format;
    private AuthenticationType authenticationType;
    private Boolean active;
    private Integer timeoutSeconds;
    private Integer retryAttempts;
    private List<FieldMappingDTO> fieldMappings;
} 