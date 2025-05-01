package com.davinci.geromercante.marketing.module.auth.service.impl;

import com.davinci.geromercante.marketing.module.auth.manager.SessionManager;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.auth.service.SessionService;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionManager sessionManager;

    @Override
    @Transactional
    public void saveSession(Credential credential, User user, Profile profile, String token) {
        sessionManager.saveSession(credential, user, profile, token);
    }

    @Override
    @Transactional
    public void revokeToken(String token) {
        sessionManager.revokeToken(token);
    }

    @Override
    @Transactional
    public boolean isTokenRevoked(String token) {
        return sessionManager.isTokenRevoked(token);
    }
}
