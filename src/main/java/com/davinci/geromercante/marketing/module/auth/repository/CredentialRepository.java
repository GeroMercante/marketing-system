package com.davinci.geromercante.marketing.module.auth.repository;

import com.davinci.geromercante.marketing.common.repository.BaseRepository;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends BaseRepository<Credential, Long> {
    @Query("SELECT c FROM Credential c WHERE c.user.id = :id AND c.typeCredential = :typeCredential AND c.deleted = false")
    Optional<Credential> findByUserIdAndTypeCredential(@Param("id") Long id, @Param("typeCredential") CredentialTypeEnum typeCredential);

    @Query("SELECT c FROM Credential c WHERE c.user.email = :email AND c.typeCredential = :typeCredential AND c.deleted = false")
    Optional<Credential> findByUserEmailAndTypeCredential(@Param("email") String email, @Param("typeCredential") CredentialTypeEnum typeCredential);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Credential c WHERE c.user.email = :email AND c.typeCredential = :typeCredential AND c.deleted = false")
    boolean existsByUser_EmailAndTypeCredential(@Param("email") String email, @Param("typeCredential") CredentialTypeEnum typeCredential);
}
