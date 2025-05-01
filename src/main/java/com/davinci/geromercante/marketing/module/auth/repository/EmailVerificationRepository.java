package com.davinci.geromercante.marketing.module.auth.repository;

import com.davinci.geromercante.marketing.common.repository.BaseRepository;
import com.davinci.geromercante.marketing.module.auth.model.entity.EmailVerification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends BaseRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);
}
