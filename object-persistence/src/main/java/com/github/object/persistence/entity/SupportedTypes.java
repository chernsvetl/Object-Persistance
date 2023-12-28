package com.github.object.persistence.entity;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

public class SupportedTypes {
    private SupportedTypes() {
    }

    public static void validateSupportedType(Class<?> objectType) {
        validateSupportedType(objectType, new IllegalArgumentException(String.format("Given type %s not supported", objectType)));
    }

    public static void validateSupportedType(Class<?> objectType, RuntimeException exception) {
        boolean condition = Number.class.isAssignableFrom(objectType) || objectType.isPrimitive() || String.class.equals(objectType) ||
                Instant.class.equals(objectType) || Date.class.equals(objectType) || LocalTime.class.equals(objectType) ||
                Boolean.class.equals(objectType) || byte[].class.equals(objectType);
        if (!condition) {
            throw exception;
        }
    }
}
