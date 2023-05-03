package com.greengoldfish.util;

import com.greengoldfish.domain.Authority;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class RepositoryCleanerService {

    private final EntityManager entityManager;
    private List<String> tableNames;
    private List<String> otherTables;
    private final List<String> TABLES_TO_IGNORE = List.of(
            "tag"
    );

    public RepositoryCleanerService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(Predicate.not(it -> Authority.class.equals(it.getJavaType())))
                .map(entityType -> entityType.getJavaType().getAnnotation(Table.class))
                .filter(Objects::nonNull)
                .map(this::convertToTableName)
                .filter(tableName -> !TABLES_TO_IGNORE.contains(tableName))
                .toList();
        otherTables = List.of("user_authority");
    }

    @Transactional
    public void resetDatabase() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE;").executeUpdate();

        tableNames.stream()
                .filter(this::hasData)
                .forEach(this::truncateTable);

        otherTables.stream()
                .filter(this::hasData)
                .forEach(this::truncateTable);

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE;").executeUpdate();
    }

    private void truncateTable(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    }

    private boolean hasData(String tableName) {
        Object result = entityManager.createNativeQuery("SELECT EXISTS(SELECT 1 FROM " + tableName + ")").getSingleResult();
        if (Objects.isNull(result)) {
            return false;
        }

        if (result instanceof Boolean) {
            return (Boolean) result;
        }

        int found = ((BigInteger) result).intValue();
        return found != 0;
    }

    /**
     * Converts an (optional) schema and table on a {@link Table} annotation to sql table name.
     */
    private String convertToTableName(Table table) {
        String schema = table.schema();
        String tableName = table.name();

        String convertedSchema = StringUtils.hasText(schema) ? schema.toLowerCase() + "." : "";
        String convertedTableName = tableName.replaceAll("([a-z])([A-Z])", "$1_$2");

        return convertedSchema + convertedTableName;
    }
}
