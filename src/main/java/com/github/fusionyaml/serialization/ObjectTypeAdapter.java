package com.github.fusionyaml.serialization;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.exceptions.YamlDeserializationException;
import com.github.fusionyaml.exceptions.YamlSerializationException;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlObject;
import com.github.fusionyaml.utils.ReflectionUtils;
import com.github.fusionyaml.utils.YamlUtils;
import com.google.common.reflect.TypeToken;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ObjectTypeAdapter<T> extends TypeAdapter<T> {

    private Objenesis genesis = new ObjenesisStd();
    private ObjectSerializationManager instantiator = new ObjectSerializationManager(fusionYAML);

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
