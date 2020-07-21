package io.github.fusionyaml.annotations;

import io.github.fusionyaml.adapters.ObjectTypeAdapter;
import io.github.fusionyaml.adapters.PrimitiveTypeAdapter;
import io.github.fusionyaml.adapters.TypeAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomAdapter {

    Class<? extends TypeAdapter> value();

}
