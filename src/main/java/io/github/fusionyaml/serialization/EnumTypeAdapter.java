package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlPrimitive;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {

    private ObjectInstantiator instantiator = new ObjectInstantiator(fusionYAML);

    public EnumTypeAdapter(FusionYAML fusionYAML) {
        super(fusionYAML);
    }

    public EnumTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public YamlElement serialize(T obj, Type type) {
        try {
            Field[] fields = obj.getClass().getFields();
            Field field = null;
            for (Field f : fields) {
                if (f.get(null).equals(obj)) field = f;
            }
            if (field == null) throw new YamlSerializationException("Can't find field for enum object " + obj);
            return new YamlPrimitive(obj.getDeclaringClass().getName() + "." +
                    instantiator.getSerializedName(field));
        } catch (Exception e) {
            throw new YamlSerializationException(e);
        }
    }

    @Override
    public T deserialize(YamlElement element, Type type) {
        Class<T> tEnum = (Class<T>) type;
        String name = element.getAsString();
        return Enum.valueOf(tEnum, name.replace(tEnum.getDeclaringClass().getName(), ""));
    }
}
