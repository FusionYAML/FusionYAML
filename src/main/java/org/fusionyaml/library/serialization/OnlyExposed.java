package org.fusionyaml.library.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation restricts the {@link ObjectTypeAdapter} from serializing
 * and deserializing fields that aren't annotated with {@link Expose} in the
 * classes annotated with {@link OnlyExposed}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyExposed {
}
