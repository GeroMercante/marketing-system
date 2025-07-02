package com.davinci.geromercante.marketing.module.datasource.model.dto.request;

import com.davinci.geromercante.marketing.module.datasource.model.dto.FieldMappingDTO;
import com.davinci.geromercante.marketing.module.datasource.model.enums.AuthenticationType;
import com.davinci.geromercante.marketing.module.datasource.model.enums.ResponseFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceRequestDTO {
    private String name;
    private String description;
    private String url;
    private ResponseFormat format;
    private AuthenticationType authenticationType;
    private String apiKey;
    private String username;
    private String password;
    private String bearerToken;
    private Boolean active = Boolean.TRUE;
    private Integer timeoutSeconds = 30;
    private Integer retryAttempts = 3;
    private List<FieldMappingDTO> fieldMappings;
} 