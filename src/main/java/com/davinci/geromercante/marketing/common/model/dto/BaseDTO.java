package com.davinci.geromercante.marketing.common.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDTO {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private Date createdAt;
    private Date updatedAt;
}
