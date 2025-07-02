package com.davinci.geromercante.marketing.module.datasource.repository;

import com.davinci.geromercante.marketing.common.repository.BaseRepository;
import com.davinci.geromercante.marketing.module.datasource.model.entity.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataSourceRepository extends BaseRepository<DataSource, Long> {
    @Query("SELECT ds FROM DataSource ds " +
            "WHERE (:searchText IS NULL OR " +
            "LOWER(ds.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(ds.description) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(ds.url) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND ds.deleted = false")
    Page<DataSource> search(
            @Param("searchText") String searchText,
            Pageable pageable
    );

    List<DataSource> findByActiveAndDeleted(Boolean active, Boolean deleted);
    
    Optional<DataSource> findByNameAndDeleted(String name, Boolean deleted);
    
    Optional<DataSource> findByUrlAndDeleted(String url, Boolean deleted);
} 
