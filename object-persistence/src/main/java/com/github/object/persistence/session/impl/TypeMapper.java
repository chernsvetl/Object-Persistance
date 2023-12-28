package com.github.object.persistence.session.impl;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.SQLType;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

public final class TypeMapper {

    private static final Map<Class<?>, SQLType> types = initMap();

    private TypeMapper() {}

    //как минимум надо добавить timestamp_with_timezone
    private static Map<Class<?>, SQLType> initMap() {
        return Map.ofEntries(
                Map.entry(LocalTime.class, JDBCType.TIME),
                Map.entry(Float.class, JDBCType.REAL),
                Map.entry(float.class, JDBCType.REAL),
                Map.entry(Long.class, JDBCType.BIGINT),
                Map.entry(long.class, JDBCType.BIGINT),
                Map.entry(byte[].class, JDBCType.BINARY),
                Map.entry(Instant.class, JDBCType.TIMESTAMP),
                Map.entry(Short.class, JDBCType.SMALLINT),
                Map.entry(short.class, JDBCType.SMALLINT),
                Map.entry(String.class, JDBCType.VARCHAR),
                Map.entry(Integer.class, JDBCType.INTEGER),
                Map.entry(int.class, JDBCType.INTEGER),
                Map.entry(Double.class, JDBCType.DOUBLE),
                Map.entry(double.class, JDBCType.DOUBLE),
                Map.entry(Date.class, JDBCType.DATE),
                Map.entry(Byte.class, JDBCType.TINYINT),
                Map.entry(byte.class, JDBCType.TINYINT),
                Map.entry(Boolean.class, JDBCType.BIT),
                Map.entry(boolean.class, JDBCType.BIT),
                Map.entry(BigDecimal.class, JDBCType.NUMERIC)
        );
    }

    public static String getSQLTypeString(Class<?> javaType) {
        return getSQLType(javaType).getName();
    }

    public static SQLType getSQLType(Class<?> javaType) {
        return types.get(javaType);
    }
}
