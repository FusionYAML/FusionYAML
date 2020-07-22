package io.github.fusionyaml.serialization;

import io.github.fusionyaml.serialization.TypeAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomAdapter {

    Class<? extends TypeAdapter> value();

}
