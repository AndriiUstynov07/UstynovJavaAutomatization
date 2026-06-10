package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Maps a class to a database table.
 * Retained at RUNTIME so the ORM session can read it via reflection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Table {
    /** Table name; defaults to the simple class name in lower-case. */
    String name() default "";
}
