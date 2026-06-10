package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Marks an entity as requiring runtime validation before persistence.
 * Retained at RUNTIME — the OrmSession checks this annotation via
 * reflection before calling save() / update().
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Validated {
    /** Whether to throw on the first violation (true) or collect all (false). */
    boolean failFast() default true;
}
