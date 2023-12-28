package com.github.object.persistence.persistence.common;

import com.github.object.persistence.query.Predicate;
import com.github.object.persistence.query.Query;
import com.github.object.persistence.session.AbstractSession;
import com.github.object.persistence.query.impl.QueryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Тестовые сценарии проверки класса {@link QueryImpl}.
 */
class QueryImplTest {

    private Query<?> query;
    private AbstractSession session;
    private Class<?> clazz;
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        session = Mockito.mock(AbstractSession.class);
        clazz = Class.class;
        query = QueryImpl.getQuery(session, clazz);
    }

    @Test
    void select_predicateIsNotNull() {
        String predicateAsString = "var IS NOT NULL";
        when(predicate.toString()).thenReturn(predicateAsString);

        query.selectWhere(predicate);

        Mockito.verify(session, Mockito.only())
                .getRecords(clazz, Optional.of(predicateAsString));
    }

    @Test
    void select_predicateIsEmpty() {
        query.selectWhere(predicate);

        Mockito.verify(session, Mockito.only())
                .getRecords(clazz, Optional.empty());
    }

    @Test
    void select_predicateIsNull() {
        query.selectWhere(null);

        Mockito.verify(session, Mockito.only())
                .getRecords(clazz, Optional.empty());
    }

    @Test
    void update_predicateIsNotNull() {
        String predicateAsString = "var IS NOT NULL";
        Map<String, Object> fieldValueMap = Map.of("var1", "val1");
        when(predicate.toString()).thenReturn(predicateAsString);

        query.updateWhere(fieldValueMap, predicate);

        Mockito.verify(session, Mockito.only())
                .updateRecord(clazz, fieldValueMap, Optional.of(predicateAsString));
    }

    @Test
    void update_predicateIsEmpty() {
        Map<String, Object> fieldValueMap = Map.of("var1", "val1");

        query.updateWhere(fieldValueMap, predicate);

        Mockito.verify(session, Mockito.only())
                .updateRecord(clazz, fieldValueMap, Optional.empty());
    }

    @Test
    void update_predicateIsNull() {
        Map<String, Object> fieldValueMap = Map.of("var1", "val1");

        query.updateWhere(fieldValueMap, null);

        Mockito.verify(session, Mockito.only())
                .updateRecord(clazz, fieldValueMap, Optional.empty());
    }

    @Test
    void update_fieldValueMapIsNotEmpty() {
        Map<String, Object> filedValueMap = new HashMap<>();
        filedValueMap.put("var1", "val1");
        filedValueMap.put(null, "val2");
        filedValueMap.put("var3", "val3");
        String predicateAsString = "var IS NOT NULL";
        when(predicate.toString()).thenReturn(predicateAsString);

        query.updateWhere(filedValueMap, predicate);

        Mockito.verify(session, Mockito.only())
                .updateRecord(
                        clazz,
                        Map.of("var1", "val1", "var3", "val3"),
                        Optional.of(predicateAsString)
                );
    }

    @Test
    void update_fieldValueMapIsEmpty() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> query.updateWhere(Map.of(), predicate));

        Assertions.assertEquals(
                "Значение 'fieldValueMap' не должно быть пустым",
                exception.getMessage()
        );
    }

    @Test
    void update_fieldValueMapContainsOnlyNullKey() {
        Map<String, Object> fieldValueMap = new HashMap<>();
        fieldValueMap.put(null, "val");

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> query.updateWhere(fieldValueMap, predicate)
        );

        Assertions.assertEquals(
                "Значение 'fieldValueMap' не должно быть пустым",
                exception.getMessage()
        );
    }

    @Test
    void update_fieldValueMapIsNull() {
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> query.updateWhere(null, predicate)
        );

        Assertions.assertEquals(
                "Значение 'fieldValueMap' не должно быть null",
                exception.getMessage()
        );
    }

    @Test
    void delete_predicateIsNotNull() {
        String predicateAsString = "var IS NOT NULL";
        when(predicate.toString()).thenReturn(predicateAsString);

        query.deleteWhere(predicate);

        Mockito.verify(session, Mockito.only())
                .deleteRecord(clazz, Optional.of(predicateAsString));
    }

    @Test
    void delete_predicateIsEmpty() {
        query.deleteWhere(predicate);

        Mockito.verify(session, Mockito.only())
                .deleteRecord(clazz, Optional.empty());
    }

    @Test
    void delete_predicateIsNull() {
        query.deleteWhere(null);

        Mockito.verify(session, Mockito.only())
                .deleteRecord(clazz, Optional.empty());
    }

}
