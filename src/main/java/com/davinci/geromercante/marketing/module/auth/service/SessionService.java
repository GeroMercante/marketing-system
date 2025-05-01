package com.davinci.geromercante.marketing.module.auth.service;

import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.user.model.entity.User;

public interface SessionService {
    void saveSession(Credential credential, User user, Profile profile, String token);
    void revokeToken(String token);
    boolean isTokenRevoked(String token);
}
