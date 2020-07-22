package io.github.fusionyaml.serialization;

import com.google.common.reflect.TypeToken;
import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.utils.ReflectionUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ObjectTypeAdapter<T> extends TypeAdapter<T> {

    private Objenesis genesis = new ObjenesisStd();
    private ObjectInstantiator instantiator = new ObjectInstantiator(fusionYAML);

    public ObjectTypeAdapter(FusionYAML yaml) {
        super(yaml);
    }

    public ObjectTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public YamlElement serialize(T obj, Type type) {
        TypeAdapter<T> adapter = fusionYAML.getTypeAdapter(type);
        if (!(adapter instanceof ObjectTypeAdapter)) return adapter.serialize(obj, type);
        // No specific type adapter for this type
        // obtain name & values from field, store them
        // and then create a YamlObject containing these values
        try {
            List<Field> fields = instantiator.getFieldsForSerialization(type);
//            Map<String, YamlElement> map = new LinkedHashMap<>();
//            for (Field field : fields) {
//                boolean prev = field.isAccessible();
//                field.setAccessible(true);
//                map.put(field.getName(), fusionYAML.serialize(field.get(obj), obj.getClass()));
//                field.setAccessible(prev);
//            }
            return new YamlObject(instantiator.toSerializedMap(obj, type), fusionYAML);
        } catch (Exception e) {
            throw new YamlSerializationException(e);
        }
    }

    @Override
    public T deserialize(YamlElement element, Type type) {
        TypeAdapter<T> adapter = fusionYAML.getTypeAdapter(type);
        if (!(adapter instanceof ObjectTypeAdapter)) return adapter.deserialize(element, type);
        // No specific type adapter for this type. attempt to get values of fields
        // and try to assign to them if values are a map but field type is a nonessential
        // object, then the values in the inner map will be assigned to the object
        Map<String, Object> map = YamlUtils.toMap0(element.getAsYamlObject());
        T obj = genesis.newInstance((Class<T>) TypeToken.of(type).getRawType());
        List<Field> fields = ReflectionUtils.getNonStaticFields(obj);
        if (!ReflectionUtils.isMatch(map, fields))
            throw new YamlDeserializationException("The map passed in is not the deserialized object type");
        instantiator.assignFields(obj, map, type);
        return obj;
    }
}
