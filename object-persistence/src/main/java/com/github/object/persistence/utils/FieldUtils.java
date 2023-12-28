package com.github.object.persistence.utils;

import com.github.object.persistence.core.EntityCash;

import java.lang.reflect.Field;

public class FieldUtils {
    private FieldUtils() {
    }

    public static String getForeignKeyName(Field field) {
        return String.format("%s_id", field.getName());
    }

    public static Field getIdField(Field field) {
        return getIdField(field.getType());
    }

    public static String getIdName(Class<?> kClass) {
        return getIdField(kClass).getName();
    }

    public static String getTableNameOfFieldClass(Field field) {
        return EntityCash.getEntityInfo(field.getType()).getEntityName();
    }

    public static Field getIdField(Class<?> kClass) {
        return EntityCash.getEntityInfo(kClass).getIdField();
    }
}
