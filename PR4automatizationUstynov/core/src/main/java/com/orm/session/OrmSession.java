package com.orm.session;

import com.orm.annotations.*;
import com.orm.runtime.EntityValidator;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Minimal in-memory ORM session.
 * Uses @Table, @Id, @Column at RUNTIME to build SQL-like logs and manage entities.
 */
public class OrmSession {

    // tableName -> (id -> entity)
    private final Map<String, Map<Long, Object>> store = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();
    private final EntityValidator validator = new EntityValidator();

    // ------------------------------------------------------------------ save
    public <T> T save(T entity) {
        validator.validate(entity);

        Class<?> clazz = entity.getClass();
        String table = resolveTable(clazz);

        Field idField = findIdField(clazz);
        long id = sequences.computeIfAbsent(table, k -> new AtomicLong(0)).incrementAndGet();

        try {
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        store.computeIfAbsent(table, k -> new LinkedHashMap<>()).put(id, entity);
        System.out.println("[ORM] INSERT INTO " + table + " " + buildInsertLog(entity));
        return entity;
    }

    // ----------------------------------------------------------------- findById
    @SuppressWarnings("unchecked")
    public <T> Optional<T> findById(Class<T> clazz, long id) {
        String table = resolveTable(clazz);
        Map<Long, Object> rows = store.getOrDefault(table, Collections.emptyMap());
        System.out.println("[ORM] SELECT * FROM " + table + " WHERE id = " + id);
        return Optional.ofNullable((T) rows.get(id));
    }

    // ----------------------------------------------------------------- findAll
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> clazz) {
        String table = resolveTable(clazz);
        System.out.println("[ORM] SELECT * FROM " + table);
        return (List<T>) new ArrayList<>(
                store.getOrDefault(table, Collections.emptyMap()).values());
    }

    // ----------------------------------------------------------------- delete
    public <T> void delete(Class<T> clazz, long id) {
        String table = resolveTable(clazz);
        store.getOrDefault(table, Collections.emptyMap()).remove(id);
        System.out.println("[ORM] DELETE FROM " + table + " WHERE id = " + id);
    }

    // ----------------------------------------------------------------- helpers
    private String resolveTable(Class<?> clazz) {
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) throw new IllegalArgumentException(clazz.getSimpleName() + " is not annotated with @Table");
        return t.name().isBlank() ? clazz.getSimpleName().toLowerCase() : t.name();
    }

    private Field findIdField(Class<?> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) return f;
        }
        throw new IllegalArgumentException("No @Id field in " + clazz.getSimpleName());
    }

    private String buildInsertLog(Object entity) {
        StringBuilder sb = new StringBuilder("(");
        for (Field f : entity.getClass().getDeclaredFields()) {
            Column col = f.getAnnotation(Column.class);
            Id id = f.getAnnotation(Id.class);
            if (col == null && id == null) continue;
            f.setAccessible(true);
            String colName = (col != null && !col.name().isBlank()) ? col.name() : f.getName();
            Object val = null;
            try { val = f.get(entity); } catch (IllegalAccessException ignored) {}
            sb.append(colName).append("=").append(val).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append(")");
        return sb.toString();
    }
}
