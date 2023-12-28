package com.github.object.persistence.persistence.sql.impl;

import com.github.object.persistence.core.EntityCache;
import com.github.object.persistence.core.EntityInfoImpl;
import com.github.object.persistence.session.impl.SqlGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlGeneratorTest {
    private final SqlGenerator generator = SqlGenerator.getInstance();

    @Test
    void insertRecords() {
        try (MockedStatic<EntityCache> mockedStatic = Mockito.mockStatic(EntityCache.class)) {
            mockedStatic.when(() -> EntityCache.getEntityInfo(TestEntity.class)).thenReturn(EntityInfoImpl.create(TestEntity.class));
            String result = generator.insertRecords(
                    TestEntity.class, 3, Set.of("date", "id")
            );
            assertEquals(
                    "INSERT INTO testentity (id, date) VALUES (?, ?), (?, ?), (?, ?);",
                    result
            );
        }
    }

    @Test
    void insertRecord() {
        try (MockedStatic<EntityCache> mockedStatic = Mockito.mockStatic(EntityCache.class)) {
            mockedStatic.when(() -> EntityCache.getEntityInfo(TestEntity.class)).thenReturn(EntityInfoImpl.create(TestEntity.class));
            String result = generator.insertRecord(TestEntity.class, Set.of("date", "id"));
            assertEquals(
                    "INSERT INTO testentity (id, date) VALUES (?, ?);",
                    result
            );
        }
    }

    @Test
    void createTable() {
        try (MockedStatic<EntityCache> mockedStatic = Mockito.mockStatic(EntityCache.class)) {
            mockedStatic.when(() -> EntityCache.getEntityInfo(TestEntity.class)).thenReturn(EntityInfoImpl.create(TestEntity.class));
            String result = generator.createTable(TestEntity.class);
            assertEquals(
                    "CREATE TABLE IF NOT EXISTS testentity (id BIGINT PRIMARY KEY, date DATE);",
                    result
            );
        }
    }

}
