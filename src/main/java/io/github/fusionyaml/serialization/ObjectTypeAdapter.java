/*
Copyright 2019 BrokenEarthDev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlNode;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.object.YamlPrimitive;
import io.github.fusionyaml.utils.ReflectionUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link Object} TypeAdapter
 */
public class ObjectTypeAdapter extends TypeAdapter {

    private static final Objenesis OBJENESIS = new ObjenesisStd();

    @Override
    public Object deserialize(@NotNull YamlElement serialized) {
        if (serialized.isYamlList())
            return FusionYAML.COLLECTION_TYPE_ADAPTER.deserialize(serialized);
        else if (serialized instanceof YamlObject || serialized instanceof YamlNode)
            return FusionYAML.MAP_TYPE_ADAPTER.deserialize(serialized);
        else if (serialized instanceof YamlPrimitive)
            return FusionYAML.PRIMITIVE_TYPE_ADAPTER.deserialize(serialized);
        else return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public YamlElement serialize(@NotNull Object o) {
        if (o instanceof Collection)
            return new CollectionTypeAdapter().serialize((Collection) o);
        else if (o instanceof Map)
            return new MapTypeAdapter().serialize((Map) o);
        else if (YamlUtils.isPrimitive(o))
            return new PrimitiveTypeAdapter().serialize(o);
        try {
            List<Field> fields = ReflectionUtils.getNonStaticFields(o);
            Map<String, YamlElement> map = new LinkedHashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(o);
                map.put(field.getName(), serialize(obj));
                field.setAccessible(false);
            }
            YamlObject serialized = new YamlObject(map);
            callSerializationEvent(o, serialized);
            return serialized;
        } catch (IllegalAccessException e) {
            throw new YamlSerializationException(e);
        }
    }

    /**
     * Deserializes a {@link YamlObject} into a specified {@link Class} of type {@link T}.
     *
     * @param object The {@link YamlObject} to deserialize
     * @param clazz The class to deserialize into
     * @param <T> The object type
     * @return The deserialized object. The deserialized object will be of type {@link T}
     */
    public <T> T deserializeObject(YamlObject object, Class<T> clazz) {
        Map<String, Object> map = YamlUtils.toMap0(object);
        T t = OBJENESIS.newInstance(clazz);
        List<Field> fields = ReflectionUtils.getNonStaticFields(t);
        boolean match = ReflectionUtils.isMatch(map, fields);
        if (!match)
            throw new YamlDeserializationException("The map passed in is not the deserialized object type");
        ReflectionUtils.assignFields(t, map, fields, this);
        callDeserializationEvent(t, object);
        return t;
    }

}
