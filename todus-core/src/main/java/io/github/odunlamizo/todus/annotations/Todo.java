package io.github.odunlamizo.todus.annotations;

import java.lang.annotation.*;

/** Marks a class, method, or field as a TODO item to be tracked. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Todo {
    String value();

    String assignee() default "";

    String due() default "";

    Priority priority() default Priority.MEDIUM;

    enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}
