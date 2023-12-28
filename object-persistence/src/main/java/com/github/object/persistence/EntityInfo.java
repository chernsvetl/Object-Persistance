package com.github.object.persistence;

import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @param <T> тип сущности
 */
public interface EntityInfo<T> {

    /*
    MethodInterceptor это перехватчик методов, так как мы не должны завязываться на реализации, он
    может и будет перехватывать любые гет-сет методы, но логика будет отлчичаться в зависимости от реализации
     */
    T getProxy(MethodInterceptor whatToProxy);

    Collection<T> getCollectionProxy(MethodInterceptor whatToProxy);

    Class<?> getFieldClassTypeByName(String fieldName);

    Set<Class<?>> getAnnotations(String fieldName);

    String getEntityName();

    Map<String, Field> getFieldNames();

    Set<Field> getFields();

    Set<Field> getTableFields();

    Set<Field> getManyToOneFields();

    Set<Field> getOneToOneFields(boolean parent);

    Set<Field> getOneToManyFields();

    Set<Field> getNoRelationFields();

    Field getIdField();
}
