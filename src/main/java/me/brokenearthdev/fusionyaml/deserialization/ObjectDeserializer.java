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
package me.brokenearthdev.fusionyaml.deserialization;

import me.brokenearthdev.fusionyaml.serialization.YamlSerializationException;
import me.brokenearthdev.fusionyaml.utils.ReflectionUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectDeserializer extends Deserializer {

    private static final Objenesis objenesis = new ObjenesisStd();

    @Override
    public <T> T deserialize(Map map, Class<T> as) throws YamlDeserializationException {
        if (as.isPrimitive() || as.equals(String.class))
            throw new YamlDeserializationException("Objects of primitive types aren't initially serialized into a map");
        T t = objenesis.newInstance(as);
        List<Field> fields = ReflectionUtils.getNonStaticFields(t);
        boolean safe = checkSafe(fields, t);
        if (!safe)
            throw new YamlDeserializationException("An object in the class is not of primitive type, " + String.class.getName() + ", " +
                    Collection.class.getName() + ", " + " and a " + Map.class.getName());
        boolean initFields = initFields(fields, t, map);
        if (!initFields)
            throw new YamlDeserializationException("The map passed in is not the class's serialized form");
        return t;
    }

    private static boolean checkSafe(List<Field> fields, Object o) throws YamlDeserializationException {
        for (Field field : fields) {
            boolean isInstance = false;
            // todo check if primitive etc
        }
        return true;
    }

    private static boolean initFields(List<Field> fields, Object o, Map map) throws YamlDeserializationException {
        if (fields.size() != map.size())
            return false;
        Map m2 = new LinkedHashMap(map);
        for (Field field : fields) {
            if (map.containsKey(field.getName())) {
                m2.remove(field.getName());
                try {
                    field.set(o, map.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new YamlDeserializationException(e);
                }
                if (m2.size() == 0)
                    return true;
            }
        }
        return false;
    }

}
