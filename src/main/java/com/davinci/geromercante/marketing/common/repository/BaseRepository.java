package com.davinci.geromercante.marketing.common.repository;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
@NonNullApi
public interface BaseRepository<T extends BaseEntity, I> extends JpaRepository<T, I> {

    @Override
    @NonNull
    @Query("select e from #{#entityName} e where e.deleted = false")
    List<T> findAll();

    @Override
    @NonNull
    @Query("select e from #{#entityName} e where e.deleted = false and e.id = ?1")
    Optional<T> findById(I id);

    @Override
    @NonNull
    @Query("select e from #{#entityName} e where e.deleted = false and e.id in :ids")
    List<T> findAllById(@Param("ids") Iterable<I> ids);

    @Override
    default void delete(T entity) {
        entity.setDeleted(true);
        save(entity);
    }

    default void deleteById(I id) {
        Optional<T> entity = findById(id);
        entity.ifPresent(this::delete);
    }

    @Override
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }
}
