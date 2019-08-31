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
package me.brokenearthdev.fusionyaml.serialization;

import me.brokenearthdev.fusionyaml.utils.ReflectionUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class's role is to serialize {@link Object}s into {@link Object}s that can
 * be written into YAML.
 */
public class ObjectSerializer extends Serializer {

    /**
     * Serializes {@link Object} into a {@link Map} of serialized {@link Object}s that can be
     * written into YAML. If a primitive {@link Object}, {@link String}, {@link List}, or {@link Map}
     * is passed, the method will serialize it by using the appropriate serializer for the {@link Object}.
     *
     * @param o The {@link Object} to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException Thrown if any reflective error(s) occurred.
     */
    @Override
    public Object serialize(Object o) throws IllegalAccessException {
        if (o instanceof Collection)
            return Serializers.COLLECTION_SERIALIZER.serialize(o);
        else if (o instanceof Map)
            return Serializers.MAP_SERIALIZER.serialize(o);
        else if (YamlUtils.isPrimitive(o))
            return Serializers.PRIMITIVE_SERIALIZER.serialize(o);
        List<Field> fields = ReflectionUtils.getFields(o);
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), new ObjectSerializer().serialize(field.get(o)));
            field.setAccessible(false);
        }
        return map;
    }

}
