package com.davinci.geromercante.marketing.module.auth.manager;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.CredentialResponse;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.repository.CredentialRepository;
import com.davinci.geromercante.marketing.module.email.messaging.EmailProducer;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialManager {

    private final CredentialRepository credentialRepository;
    private final MessageUtil messageUtil;
    private final EmailProducer emailProducer;
    private final PasswordEncoder passwordEncoder;

    public Credential getByUserAndTypeCredential(User user, CredentialTypeEnum credentialTypeEnum)  {
        return credentialRepository.findByUserIdAndTypeCredential(user.getId(), credentialTypeEnum).orElse(null);
    }

    public void createCredential(User user, CredentialResponse credentialDTO) throws MarketingException {
        Credential credential = getByUserAndTypeCredential(user, credentialDTO.getTypeCredential());

        if (credential != null) {
            throw new MarketingException(messageUtil.getMessage("credential.exist"));
        }

        credential = new Credential();
        credential.setTypeCredential(credentialDTO.getTypeCredential());
        credential.setUser(user);
        credential.setIsTemporary(credentialDTO.getIsTemporary());
        credential.setExternalReference(credentialDTO.getExternalReference());

        if (Boolean.TRUE.equals(credentialDTO.getIsTemporary())) {
            emailProducer.sendTemporaryPasswordEmail(user.getEmail(), credentialDTO.getPassword());
            credential.setPassword(passwordEncoder.encode(credentialDTO.getPassword()));
        } else {
            credential.setPassword(credentialDTO.getPassword());
        }

        credentialRepository.save(credential);
    }
}
