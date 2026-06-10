package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Runtime constraint: the annotated field must not be null when validated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface NotNull {
    String message() default "Field must not be null";
}
