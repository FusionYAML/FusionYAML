package com.github.fusionyaml.serialization;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlElement;

import java.lang.reflect.Type;

public abstract class TypeAdapter<T> {

    protected final FusionYAML fusionYAML;

    public TypeAdapter(FusionYAML fusionYAML) {
        this.fusionYAML = fusionYAML;
    }

    public abstract YamlElement serialize(T obj, Type type);

    public abstract T deserialize(YamlElement element, Type type);

}
