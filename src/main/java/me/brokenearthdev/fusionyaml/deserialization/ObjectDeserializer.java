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

import me.brokenearthdev.fusionyaml.serialization.Serializer;
import me.brokenearthdev.fusionyaml.utils.ReflectionUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectDeserializer implements Deserializer {

    private static final Objenesis objenesis = new ObjenesisStd();

    @Override
    public <T> T deserializeObject(Map map, Class<T> clazz) throws YamlDeserializationException {
        T t = objenesis.newInstance(clazz);
        List<Field> fields = ReflectionUtils.getNonStaticFields(t);
        boolean check = checkNonPrimitive(fields);
        if (!check)
            throw new YamlDeserializationException("Invalid field type is found in the class. The fields should be " +
                    "primitive, a " + String.class.getName() + ", a " + Collection.class.getName() + ", or a " + Map.class.getName());
        boolean match = isMatch(map, fields);
        if (!match)
            throw new YamlDeserializationException("The map passed in is not the deserialized object type");
        assignFields(t, map, fields);
        return t;
    }

    private static boolean checkNonPrimitive(List<Field> fields) {
        for (Field field : fields) {
            if (check(field))
                return false;
        }
        return true;
    }

    private static boolean check(Field field) {
        return !field.getType().isPrimitive() && !contains(field.getType(), Collection.class) &&
                !contains(field.getType(), Map.class) && !contains(field.getType(), String.class);
    }

    private static boolean isMatch(Map map, List<Field> fields) {
        if (map.size() != fields.size())
            return false;
        for (Field field : fields) {
            if (!map.containsKey(field.getName()))
                return false;
        }
        return true;
    }

    private static boolean contains(Class c1, Class c2) {
        return c1.equals(c2) || c1.getSuperclass() != null && contains(c1.getSuperclass(), c2) || containsInterface(c1, c2);
    }

    private static boolean containsInterface(Class c1, Class c2) {
        Class[] interfaces = c1.getInterfaces();
        for (Class clazz : interfaces) {
            if (clazz.equals(c2) || containsInterface(clazz, c2))
                return true;
        }
        return false;
    }


    private static void assignFields(Object o, Map map, List<Field> fields) throws YamlDeserializationException {
        for (Object key : map.keySet()) {
            for (Field field : fields) {
                if (key.toString().equals(field.getName())) {
                    try {
                        Object found = map.get(field.getName());
                        Object deserialized = Deserializers.OBJECT_DESERIALIZER.deserialize(found);
                        if (deserialized == null) continue;
                        field.set(o, deserialized);
                    } catch (IllegalAccessException e) {
                        throw new YamlDeserializationException(e);
                    }
                }
            }
        }
    }

    @Override
    public Object deserialize(Object serializedObj) throws YamlDeserializationException {
        if (serializedObj instanceof Collection)
            return Deserializers.COLLECTION_DESERIALIZER.deserialize(serializedObj);
        else if (serializedObj instanceof Map)
            return Deserializers.MAP_DESERIALIZER.deserialize(serializedObj);
        else if (YamlUtils.isPrimitive(serializedObj))
            return Deserializers.PRIMITIVE_DESERIALIZER.deserialize(serializedObj);
        else return null;
    }

}
