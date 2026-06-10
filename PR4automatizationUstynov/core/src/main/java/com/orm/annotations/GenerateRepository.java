package com.orm.annotations;

import java.lang.annotation.*;

/**
 * Triggers the annotation processor to generate a *Repository class
 * for this entity at compile-time.
 *
 * RetentionPolicy.SOURCE — the annotation is consumed entirely by the
 * processor and is NOT present in the compiled .class file.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface GenerateRepository {
    /**
     * Optional override for the generated class name.
     * Defaults to "<EntityName>Repository".
     */
    String className() default "";
}
