package org.fusionyaml.library.serialization;

import com.google.common.reflect.TypeToken;
import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlSerializationException;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlPrimitive;
import org.fusionyaml.library.utils.Utilities;

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
        if (!Utilities.isPrimitive(obj))
            throw new YamlSerializationException(obj + " is not of primitive or string data type");
        return new YamlPrimitive(obj);
    }

    @Override
    public Object deserialize(YamlElement element, Type type) {
        YamlPrimitive primitive = element.getAsYamlPrimitive();
        Type t = TypeToken.of(type).getRawType();
        if (t.equals(Boolean.class) || t.equals(boolean.class)) return primitive.getAsBoolean();
        if (t.equals(Character.class) || t.equals(char.class)) return primitive.getAsChar();
        if (t.equals(String.class)) return primitive.getAsString();
        if (t.equals(Number.class)) return primitive.getAsNumber();
        return element.getAsYamlPrimitive().getValue();
    }

}
