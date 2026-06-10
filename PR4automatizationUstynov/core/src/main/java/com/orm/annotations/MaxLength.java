package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Runtime constraint: the annotated String field must not exceed {@code value} characters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface MaxLength {
    int value();
    String message() default "Field exceeds maximum length";
}
