package com.davinci.geromercante.marketing.module.auth.model.enums;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import lombok.Getter;

@Getter
public enum PermissionsEnum {
    // Perfiles
    ADMIN("permissions.ADMIN"),
    SUPERADMIN("permissions.SUPERADMIN");

    private final String messageKey;

    PermissionsEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getName(MessageUtil messageUtil) {
        return messageUtil.getMessage(messageKey);
    }
}
