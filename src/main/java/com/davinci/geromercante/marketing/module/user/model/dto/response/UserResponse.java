package com.davinci.geromercante.marketing.module.user.model.dto.response;

import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Date birthDate;
    private String documentValue;
    private Boolean active;
    private RoleEnum role;
    private Long profileId;
    private String profileName;
}
