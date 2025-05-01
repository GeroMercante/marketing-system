package com.davinci.geromercante.marketing.module.auth.model.dto.response;

import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialResponse {
    private Long id;
    private String password;
    private CredentialTypeEnum typeCredential;
    private String externalReference;
    private Boolean isVerified;
    private Boolean isTemporary;
}
