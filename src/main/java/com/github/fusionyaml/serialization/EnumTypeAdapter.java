package com.github.fusionyaml.serialization;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.exceptions.YamlSerializationException;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlPrimitive;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {

    private ObjectSerializationManager instantiator = new ObjectSerializationManager(fusionYAML);

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
            String serName = instantiator.getSerializedName(field);
            String name = fusionYAML.getYamlOptions().onlyEnumNameMentioned() ? serName :
                    obj.getDeclaringClass().getName() + "." + serName;
            return new YamlPrimitive(name);
        } catch (Exception e) {
            throw new YamlSerializationException(e);
        }
    }

    @Override
    public T deserialize(YamlElement element, Type type) {
        Class<T> tEnum = (Class<T>) type;
        String name = element.getAsString();
        return Enum.valueOf(tEnum, name.replace(tEnum.getDeclaringClass().getName() + ".", ""));
    }
}
