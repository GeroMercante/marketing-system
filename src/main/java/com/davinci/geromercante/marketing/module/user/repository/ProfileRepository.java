package com.davinci.geromercante.marketing.module.user.repository;

import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
