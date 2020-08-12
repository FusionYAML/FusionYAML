package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlSerializationException;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlPrimitive;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {

    private final FusionYAML fusionYAML;
    private final ObjectSerializationManager instantiator;


    public EnumTypeAdapter(FusionYAML fusionYAML) {
        this.fusionYAML = fusionYAML;
        this.instantiator = new ObjectSerializationManager(fusionYAML);
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
