package com.davinci.geromercante.marketing.common.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseService {

    @Autowired
    protected ModelMapper modelMapper;

    /**
     * Convierte una entidad a un DTO utilizando ModelMapper
     *
     * @param <T> Tipo de la entidad
     * @param <D> Tipo del DTO
     * @param entity Entidad a convertir
     * @param outClass Clase del DTO resultante
     * @return Objeto DTO mapeado
     */
    public <T, D> D convertToDto(T entity, Class<D> outClass) {
        return entity != null ? modelMapper.map(entity, outClass) : null;
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs
     *
     * @param <T> Tipo de la entidad
     * @param <D> Tipo del DTO
     * @param entityList Lista de entidades a convertir
     * @param outClass Clase del DTO resultante
     * @return Lista de DTOs mapeados
     */
    public <T, D> List<D> convertToDto(List<T> entityList, Class<D> outClass) {
        return entityList != null ?
                entityList.stream()
                        .map(entity -> convertToDto(entity, outClass))
                        .collect(Collectors.toList()) :
                List.of();
    }
}