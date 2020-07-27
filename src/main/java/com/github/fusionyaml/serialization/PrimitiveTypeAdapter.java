package com.github.fusionyaml.serialization;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.exceptions.YamlSerializationException;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlPrimitive;
import com.github.fusionyaml.utils.YamlUtils;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

public class PrimitiveTypeAdapter extends TypeAdapter<Object> {

    public PrimitiveTypeAdapter(FusionYAML fusionYAML) {
        super(fusionYAML);
    }

    public PrimitiveTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public YamlElement serialize(Object obj, Type type) {
        if (!YamlUtils.isPrimitive(obj))
            throw new YamlSerializationException(obj + " is not of primitive or string data type");
        return new YamlPrimitive(obj);
    }

    @Override
    public Object deserialize(YamlElement element, Type type) {
        YamlPrimitive primitive = element.getAsYamlPrimitive();
        Type t = TypeToken.of(type).getRawType();
        if (t.equals(Boolean.class)) return primitive.getAsBoolean();
        if (t.equals(Character.class)) return primitive.getAsChar();
        if (t.equals(String.class)) return primitive.getAsString();
        if (t.equals(Number.class)) return primitive.getAsNumber();
        return element.getAsYamlPrimitive().getValue();
    }

}
