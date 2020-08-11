package org.fusionyaml.library.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows you to set a custom type adapter
 * to a field of your choice. During serialization and
 * deserialization, the field's type adapter will be ignored
 * and the type adapter provided in {@link #value()} will be
 * utilized.
 * <p>
 * Please make sure that there is an empty constructor in the
 * {@link TypeAdapter} or else an exception will be thrown.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface YamlAdapter {

    /**
     * @return The {@link TypeAdapter} that'll be utilized instead of
     * the type adapter for the field's type.
     */
    Class<? extends TypeAdapter> value();

}
