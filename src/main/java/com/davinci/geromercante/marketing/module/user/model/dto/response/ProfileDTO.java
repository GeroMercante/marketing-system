package com.davinci.geromercante.marketing.module.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProfileDTO {
    private Long id;
    private String name;
    private List<String> profilePermissions;
}
