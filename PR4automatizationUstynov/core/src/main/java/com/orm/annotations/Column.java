package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Maps a field to a database column.
 * Retained at RUNTIME so the session can build INSERT / SELECT statements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Column {
    String name()     default "";
    boolean nullable() default true;
    int     length()   default 255;
}
