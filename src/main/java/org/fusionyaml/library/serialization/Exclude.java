package org.fusionyaml.library.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows fields to be excluded in serialization and
 * deserialization. When the field is deserialized, the field will
 * be {@code null}.
 * <p>
 * Applying this annotation to a field is equivalent to applying
 * {@link Expose} and setting both {@link Expose#serialization()} and
 * {@link Expose#deserialization()} to false.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Exclude {
}
