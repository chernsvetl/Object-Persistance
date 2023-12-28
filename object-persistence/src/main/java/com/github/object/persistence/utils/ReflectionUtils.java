package com.github.object.persistence.utils;

import com.github.object.persistence.exception.ReflectionOperationException;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static List<Field> getIds(Class<?> entity) {
        List<Field> idFields = Arrays.stream(entity.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());

        Class<?> superclass = entity;
        while ((superclass = superclass.getSuperclass()).isAnnotationPresent(MappedSuperclass.class)) {
            Arrays.stream(superclass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .forEach(idFields::add);
        }
        return idFields;
    }

    public static Class<?> getGenericType(Field field) {
        ParameterizedType fieldListType = (ParameterizedType) field.getGenericType();
        return (Class<?>) fieldListType.getActualTypeArguments()[0];
    }

    public static Object getValueFromField(Object fieldOwner, Field field) {
        try {
            field.setAccessible(true);
            return field.get(fieldOwner);
        } catch (IllegalAccessException exception) {
            throw new ReflectionOperationException(exception);
        } finally {
            field.setAccessible(false);
        }
    }

    //stupid hack
    public static Class<?> getClassOfCollection(Collection<?> collection) {
        return collection.stream().findAny()
                .orElseThrow(() -> new IllegalStateException("Given collection is empty"))
                .getClass();
    }

    public static <T> T createEmptyInstance(Class<T> kClass) {
        try {
            return kClass.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            throw new ReflectionOperationException(exception);
        }
    }

    public static void setValueToField(Object fieldOwner, Field field, @Nullable Object fieldValue) {
        try {
            field.setAccessible(true);
            if (fieldValue == null) {
                if (field.getType() == boolean.class) {
                    field.set(fieldOwner, false);
                } else if (field.getType() == char.class || field.getType().isPrimitive()) {
                    field.set(fieldOwner, 0);
                } else {
                    field.set(fieldOwner, null);
                }
            } else {
                field.set(fieldOwner, fieldValue);
            }
        } catch (Exception exception) {
            throw new ReflectionOperationException(exception);
        } finally {
            field.setAccessible(false);
        }
    }

    public static Field getFieldByName(Class<?> kClass, String fieldName) {
        try {
            return kClass.getDeclaredField(fieldName);
        } catch (Exception exception) {
            throw new ReflectionOperationException(exception);
        }
    }
}
