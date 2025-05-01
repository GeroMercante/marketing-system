package com.davinci.geromercante.marketing.module.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends TokenResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String profile;
    private String email;
    private Date birthDate;
    private Boolean isCredentialTemporary;
    private List<String> authorities;
}
