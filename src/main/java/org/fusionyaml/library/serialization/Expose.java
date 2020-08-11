package org.fusionyaml.library.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows fields to be exposed for serialization and/or
 * deserialization. By default, all fields are exposed for serialization
 * and deserialization except for classes annotated with {@link OnlyExposed}.
 * <p>
 * The annotation contains two methods whose default is true: {@link #serialization()}
 * and {@link #deserialization()}. You can customize the methods to achieve a
 * desired objective.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Expose {

    /**
     * @return {@code true} (default) if the field is exposed for serialization and
     * {@code false} if not.
     */
    boolean serialization() default true;

    /**
     * @return {@code true} (default) if the field is exposed for deserialization and
     * {@code false} if not.
     */
    boolean deserialization() default true;

}
