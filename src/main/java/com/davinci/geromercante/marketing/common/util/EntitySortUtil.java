package com.davinci.geromercante.marketing.common.util;

import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;

public class EntitySortUtil {

    private EntitySortUtil() {}

    public static Sort getSortForEntity(Class<?> entityClass, String orderBy, String orderDirection) {
        Sort.Direction direction = Sort.Direction.fromString(orderDirection);
        if (orderBy != null && hasProperty(entityClass, orderBy)) {
            return Sort.by(direction, orderBy);
        } else {
            return Sort.unsorted();
        }
    }

    public static boolean hasProperty(Class<?> clazz, String propertyName) {
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(propertyName)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

}
