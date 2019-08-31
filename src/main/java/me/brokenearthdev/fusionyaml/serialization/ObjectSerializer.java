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

import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ObjectSerializer extends Serializer {

    @Override
    public Object serialize(Object o) throws IllegalAccessException {
        if (o instanceof Collection)
            return Serializers.COLLECTION_SERIALIZER.serialize(o);
        else if (o instanceof Map)
            return Serializers.MAP_SERIALIZER.serialize(o);
        else if (YamlUtils.isPrimitive(o))
            return Serializers.PRIMITIVE_SERIALIZER.serialize(o);

        List<Field> fields = getFields(o);
        Map<String, Object> map = new LinkedHashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), new ObjectSerializer().serialize(field.get(o)));
            field.setAccessible(false);
        }

        return map;
    }

    private static List<Field> getFields(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Field> list = new LinkedList<>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            list.add(field);
        }
        return list;
    }

}
