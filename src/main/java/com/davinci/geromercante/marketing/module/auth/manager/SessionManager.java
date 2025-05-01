package com.davinci.geromercante.marketing.module.auth.manager;

import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.auth.model.entity.Session;
import com.davinci.geromercante.marketing.module.auth.repository.SessionRepository;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRepository sessionRepository;

    public void saveSession(Credential credential, User user, Profile profile, String token) {
        Session newSession = new Session();
        newSession.setCredential(credential);
        newSession.setUser(user);
        newSession.setProfile(profile);
        newSession.setToken(token);
        sessionRepository.save(newSession);
    }

    public void revokeToken(String token) {
        Optional<Session> sessionOpt = sessionRepository.findByToken(token);
        sessionOpt.ifPresent(session -> {
            session.setRevoked(true);
            sessionRepository.save(session);
        });
    }

    public boolean isTokenRevoked(String token) {
        return sessionRepository.findByToken(token)
                .map(Session::isRevoked)
                .orElse(true);
    }
}
