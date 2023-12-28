package com.github.object.persistence.session.impl;

import com.github.object.persistence.entity.EntityInfo;
import com.github.object.persistence.entity.EntityCache;
import com.github.object.persistence.utils.FieldUtils;
import com.github.object.persistence.utils.StringUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlGenerator {

    private static final String INSERT_INTO = "INSERT INTO %s (";
    private static final String VALUES = "VALUES";
    private static final String VALUES_WITH_PARENTHESIS = VALUES + " (";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String CLOSE_PARENTHESIS_END = CLOSE_PARENTHESIS + ";";
    private static final String SEPARATOR = ", ";
    private static final String OPEN_PARENTHESIS = "(";
    private static final String CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS %s (";
    private static final String FOREIGN_KEY = "FOREIGN KEY(%s) REFERENCES %s(%s)";
    private static final String UNIQUE = "UNIQUE";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String SELECT = "SELECT * FROM %s";
    private static final String WHERE = "WHERE";
    private static final String DELETE = "DELETE FROM %s";
    private static final String UPDATE = "UPDATE %s";
    private static final String SET = "SET";
    private static final String PREDICATE = "%s = ?";
    private static final String CONSTRAINT = "CONSTRAINT";
    private static final String FK_CONSTRAINT = "FK_%s_%s";

    private SqlGenerator() {
    }

    private static final SqlGenerator INSTANCE = new SqlGenerator();

    public static SqlGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Создает таблицу на основе переданного записи
     *
     * @param entityClass класс с описанием таблицы
     * @return сгенерированный SQL-код
     */
    public <T> String createTable(Class<T> entityClass) {
        EntityInfo<?> info = EntityCache.getEntityInfo(entityClass);
        Stream<String> parentRelation = Stream.concat(
                info.getOneToOneFields(true).stream()
                        .map(field -> StringUtils.separateWithSpace(
                                FieldUtils.getForeignKeyName(field),
                                getIdSqlType(field),
                                UNIQUE,
                                SEPARATOR,
                                CONSTRAINT,
                                String.format(FK_CONSTRAINT, FieldUtils.getTableNameOfFieldClass(field), info.getEntityName()),
                                prepareForeignKeyReference(field)
                        )),
                info.getManyToOneFields().stream()
                        .map(field -> StringUtils.separateWithSpace(
                                FieldUtils.getForeignKeyName(field),
                                getIdSqlType(field),
                                SEPARATOR,
                                CONSTRAINT,
                                String.format(FK_CONSTRAINT, FieldUtils.getTableNameOfFieldClass(field), info.getEntityName()),
                                prepareForeignKeyReference(field)
                        ))
        );
        Stream<String> noRelationStream = info.getNoRelationFields().stream()
                .map(this::getTypeAndNameForUnrelatedField);

        String tableName = info.getEntityName();
        if (tableName.equalsIgnoreCase(VALUES) ||
                tableName.equalsIgnoreCase(WHERE) ||
                tableName.equalsIgnoreCase(SET) ||
                tableName.equalsIgnoreCase(UNIQUE) ||
                tableName.equalsIgnoreCase(CONSTRAINT) ||
                tableName.equalsIgnoreCase("TABLE")) {
            tableName = "_" + tableName + "_";
        }
        return prepareScriptWithColumns(
                Stream.concat(parentRelation, noRelationStream),
                String.format(CREATE_SCRIPT, tableName),
                CLOSE_PARENTHESIS_END
        );
    }

    /**
     * Вставляет запись в существующую таблицу
     *
     * @param entity запись, которую необходимо вставить
     * @return сгенерированный SQL-код
     */
    public String insertRecord(Class<?> entity, Set<String> columnNames) {
        EntityInfo<?> info = EntityCache.getEntityInfo(entity);
        String firstPartOfScript = prepareScriptWithColumns(
                columnNames.stream(),
                String.format(INSERT_INTO, info.getEntityName()),
                CLOSE_PARENTHESIS
        );
        String values = prepareScriptWithColumns(
                info.getTableFields().stream().map(field -> "?"),
                VALUES_WITH_PARENTHESIS,
                CLOSE_PARENTHESIS_END
        );

        return StringUtils.separateWithSpace(firstPartOfScript, values);
    }

    public String insertRecords(Class<?> recordItemClass, int sizeOfItems, Set<String> columnNames) {
        EntityInfo<?> info = EntityCache.getEntityInfo(recordItemClass);
        String firstPartOfScript = prepareScriptWithColumns(
                columnNames.stream(),
                String.format(INSERT_INTO, info.getEntityName()),
                CLOSE_PARENTHESIS
        );

        String oneValuesRow = prepareScriptWithColumns(
                info.getTableFields().stream().map(field -> "?"),
                OPEN_PARENTHESIS,
                CLOSE_PARENTHESIS + SEPARATOR
        );

        String result = StringUtils.separateWithSpace(firstPartOfScript, VALUES, oneValuesRow.repeat(sizeOfItems)).strip();
        return result.substring(0, result.length() - 1) + ";";
    }

    String getFromTableWithPredicate(Class<?> kClass, Optional<String> predicate) {
        EntityInfo<?> info = EntityCache.getEntityInfo(kClass);
        return predicate.map(s -> createWithWhereScript(String.format(SELECT, info.getEntityName()), s))
                .orElseGet(() -> String.format(SELECT, info.getEntityName()));
    }

    String deleteByPredicate(Class<?> kClass, String predicate) {
        EntityInfo<?> info = EntityCache.getEntityInfo(kClass);
        return createWithWhereScript(String.format(DELETE, info.getEntityName()), predicate);
    }

    String joinConditions(Collection<String> conditions) {
        return String.join(" AND ", conditions);
    }

    String updateByPredicate(Class<?> kClass, Set<String> columnNames, Optional<String> predicate) {
        EntityInfo<?> info = EntityCache.getEntityInfo(kClass);
        String update = StringUtils.separateWithSpace(
                String.format(UPDATE, info.getEntityName()),
                SET,
                columnNames.stream().map(name -> String.format(PREDICATE, name)).collect(Collectors.joining(SEPARATOR))
        );
        return predicate.map(p -> createWithWhereScript(update, p)).orElse(update);
    }

    private String createWithWhereScript(String prefix, String predicate) {
        return StringUtils.separateWithSpace(prefix, WHERE, predicate);
    }

    private String prepareForeignKeyReference(Field field) {
        EntityInfo<?> info = EntityCache.getEntityInfo(field.getType());
        String entityName = info.getEntityName();
        String idName = info.getIdField().getName();
        return String.format(FOREIGN_KEY, FieldUtils.getForeignKeyName(field), entityName, idName);
    }

    private String getIdSqlType(Field field) {
        Field id = FieldUtils.getIdField(field);

        return TypeMapper.getSQLTypeString(id.getType());
    }

    private String getTypeAndNameForUnrelatedField(Field field) {
        String sqlType = TypeMapper.getSQLTypeString(field.getType());
        String fieldName = field.getName();
        if (sqlType == null) {
            String message = String.format("Unexpected type of entity %s field %s", field.getDeclaringClass().getName(), field.getName());
            throw new IllegalStateException(message);
        }
        if (field.isAnnotationPresent(Id.class)) {
            return StringUtils.separateWithSpace(fieldName, sqlType, PRIMARY_KEY);
        }
        return StringUtils.separateWithSpace(fieldName, sqlType);
    }

    private String prepareScriptWithColumns(Stream<String> columnNames, String prefix, String suffix) {
        return columnNames.collect(Collectors.joining(SEPARATOR, prefix, suffix));
    }

}
