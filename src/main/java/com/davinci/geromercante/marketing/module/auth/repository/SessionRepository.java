package com.davinci.geromercante.marketing.module.auth.repository;

import com.davinci.geromercante.marketing.module.auth.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByToken(String token);
    List<Session> findAllByCreatedAtBefore(Date cutoffDate);
}
