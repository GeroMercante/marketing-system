package com.davinci.geromercante.marketing.module.auth.service.impl;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements UserDetailsService {

    private final CredentialRepository credentialRepository;
    private final MessageUtil messageUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        Credential credential = credentialRepository.findByUserEmailAndTypeCredential(email, CredentialTypeEnum.INTERNAL).orElse(null);

        if (credential == null) {
            credential = credentialRepository.findByUserEmailAndTypeCredential(email, CredentialTypeEnum.GOOGLE).orElse(null);
        }

        if (credential == null) {
            throw new UsernameNotFoundException(messageUtil.getMessage("credential.not.found"));
        }

        return credential;
    }

}
