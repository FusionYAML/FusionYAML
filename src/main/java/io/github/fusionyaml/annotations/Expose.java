package io.github.fusionyaml.annotations;

public @interface Expose {

    boolean serialization() default true;
    boolean deserialization() default true;

}
