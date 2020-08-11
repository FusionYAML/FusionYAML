package org.fusionyaml.library.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows the target field's name to be named to
 * to the value set in {@link #value()} during serialization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializedName {

    /**
     * @return The field's custom name
     */
    String value();

}
