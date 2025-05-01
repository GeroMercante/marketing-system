package com.davinci.geromercante.marketing.module.user.repository;

import com.davinci.geromercante.marketing.module.auth.model.entity.ProfilePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePermissionRepository extends JpaRepository<ProfilePermission, Long> {
}
