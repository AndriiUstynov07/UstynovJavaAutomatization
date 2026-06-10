package com.orm.runtime;

import com.orm.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Runtime validator.
 * Reads @Validated, @NotNull and @MaxLength via reflection and
 * collects or throws on violations.
 */
public class EntityValidator {

    public void validate(Object entity) {
        Class<?> clazz = entity.getClass();

        Validated validated = clazz.getAnnotation(Validated.class);
        if (validated == null) return;   // nothing to do

        boolean failFast = validated.failFast();
        List<String> violations = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = null;
            try { value = field.get(entity); } catch (IllegalAccessException ignored) {}

            // --- @NotNull ---
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (notNull != null && value == null) {
                String msg = "[" + field.getName() + "] " + notNull.message();
                if (failFast) throw new ValidationException(List.of(msg));
                violations.add(msg);
            }

            // --- @MaxLength ---
            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (maxLength != null && value instanceof String str) {
                if (str.length() > maxLength.value()) {
                    String msg = "[" + field.getName() + "] " + maxLength.message()
                            + " (max=" + maxLength.value() + ", actual=" + str.length() + ")";
                    if (failFast) throw new ValidationException(List.of(msg));
                    violations.add(msg);
                }
            }

            // --- @Column(nullable=false) ---
            Column col = field.getAnnotation(Column.class);
            if (col != null && !col.nullable() && value == null) {
                String msg = "[" + field.getName() + "] column is NOT NULL but value is null";
                if (failFast) throw new ValidationException(List.of(msg));
                violations.add(msg);
            }
        }

        if (!violations.isEmpty()) throw new ValidationException(violations);
    }
}
