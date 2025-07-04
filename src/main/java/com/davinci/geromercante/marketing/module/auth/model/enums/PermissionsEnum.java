package com.davinci.geromercante.marketing.module.auth.model.enums;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import lombok.Getter;

@Getter
public enum PermissionsEnum {
    USERS_READ("permissions.USERS_READ"),
    USERS_READ_WRITE("permissions.USERS_READ_WRITE"),
    REPORTS_READ("permissions.REPORTS_READ"),
    REPORTS_READ_WRITE("permissions.REPORTS_READ_WRITE"),
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
