package com.davinci.geromercante.marketing.module.product.repository;

import com.davinci.geromercante.marketing.common.repository.BaseRepository;
import com.davinci.geromercante.marketing.module.product.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
           "(:searchText IS NULL OR :searchText = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.manufacturer) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
           "LOWER(p.externalId) LIKE LOWER(CONCAT('%', :searchText, '%'))) AND " +
           "p.deleted = false")
    Page<Product> search(@Param("searchText") String searchText, Pageable pageable);

    List<Product> findByActiveAndDeleted(Boolean active, Boolean deleted);

    Optional<Product> findByExternalIdAndDataSourceIdAndDeleted(String externalId, Long dataSourceId, Boolean deleted);

    List<Product> findByDataSourceIdAndDeleted(Long dataSourceId, Boolean deleted);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.dataSourceId = :dataSourceId AND p.deleted = false")
    long countByDataSourceId(@Param("dataSourceId") Long dataSourceId);
} 