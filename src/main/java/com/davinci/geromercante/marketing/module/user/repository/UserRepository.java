package com.davinci.geromercante.marketing.module.user.repository;

import com.davinci.geromercante.marketing.common.repository.BaseRepository;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "WHERE (:searchText IS NULL OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(u.documentValue) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND u.deleted = false")
    Page<User> search(
            @Param("searchText") String searchText,
            @Param("clientId") Long clientId,
            Pageable pageable
    );

    Optional<User> findByEmail(String email);
}
