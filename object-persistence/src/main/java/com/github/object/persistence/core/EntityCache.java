package com.github.object.persistence.core;

import org.atteo.classindex.ClassIndex;

import javax.persistence.Entity;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public final class EntityCache {

    private static final Map<Class<?>, EntityInfo<?>> cacheMap = initCacheMap();

    private EntityCache() {
    }

    private static Map<Class<?>, EntityInfo<?>> initCacheMap() {
        return StreamSupport.stream(ClassIndex.getAnnotated(Entity.class).spliterator(), false)
                .collect(Collectors.toMap(aClass -> aClass, EntityInfoImpl::create));
    }

    public static <T> EntityInfo<T> getEntityInfo(Class<T> entityClass) {
        return (EntityInfo<T>) cacheMap.get(entityClass);
    }

    public static <T> EntityInfo<T> getEntityInfo(T entity) {
        return (EntityInfo<T>) getEntityInfo(entity.getClass());
    }
}
