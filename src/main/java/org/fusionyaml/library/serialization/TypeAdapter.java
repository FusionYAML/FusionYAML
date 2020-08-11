package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;

import java.lang.reflect.Type;

public abstract class TypeAdapter<T> {

    protected final FusionYAML fusionYAML;

    public TypeAdapter(FusionYAML fusionYAML) {
        this.fusionYAML = fusionYAML;
    }

    public abstract YamlElement serialize(T obj, Type type);

    public abstract T deserialize(YamlElement element, Type type);

}
