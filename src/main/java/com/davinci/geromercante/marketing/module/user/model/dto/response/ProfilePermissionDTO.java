package com.davinci.geromercante.marketing.module.user.model.dto.response;

import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfilePermissionDTO {
    private Long id;
    private PermissionsEnum code;
    private String name;
}
