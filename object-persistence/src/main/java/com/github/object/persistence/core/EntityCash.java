package com.github.object.persistence.core;

import com.github.object.persistence.EntityInfo;
import org.atteo.classindex.ClassIndex;

import javax.persistence.Entity;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public final class EntityCash {

    private static final Map<Class<?>, EntityInfo<?>> cashMap = initCashMap();

    private EntityCash() {
    }

    private static Map<Class<?>, EntityInfo<?>> initCashMap() {
        return StreamSupport.stream(ClassIndex.getAnnotated(Entity.class).spliterator(), false)
                .collect(Collectors.toMap(aClass -> aClass, EntityInfoImpl::create));
    }

    public static <T> EntityInfo<T> getEntityInfo(Class<T> entityClass) {
        return (EntityInfo<T>) cashMap.get(entityClass);
    }

    public static <T> EntityInfo<T> getEntityInfo(T entity) {
        return (EntityInfo<T>) getEntityInfo(entity.getClass());
    }
}
