package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Marks the primary-key field of an entity.
 * Retained at RUNTIME for reflection-based ORM lookups.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Id {
    boolean autoIncrement() default true;
}
