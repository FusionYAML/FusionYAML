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
package me.brokenearthdev.fusionyaml.utils;

import me.brokenearthdev.fusionyaml.deserialization.ObjectDeserializer;
import me.brokenearthdev.fusionyaml.deserialization.YamlDeserializationException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {

    public static List<Field> getNonStaticFields(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Field> list = new LinkedList<>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            list.add(field);
        }
        return list;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class);
    }

    public static boolean checkNonPrimitive(List<Field> fields) {
        for (Field field : fields) {
            if (check(field))
                return false;
        }
        return true;
    }

    public static boolean check(Field field) {
        return !field.getType().isPrimitive() && !contains(field.getType(), Collection.class) &&
                !contains(field.getType(), Map.class) && !contains(field.getType(), String.class);
    }

    public static boolean isMatch(Map map, List<Field> fields) {
        if (map.size() != fields.size())
            return false;
        for (Field field : fields) {
            if (!map.containsKey(field.getName()))
                return false;
        }
        return true;
    }

    public static boolean contains(Class c1, Class c2) {
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


    public static void assignFields(Object o, Map map, List<Field> fields, ObjectDeserializer objDeserializer) throws YamlDeserializationException {
        for (Object key : map.keySet()) {
            for (Field field : fields) {
                if (key.toString().equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        Object found = map.get(field.getName());
                        Object deserialized = objDeserializer.deserialize(found);
                        if (deserialized == null) continue;
                        field.set(o, deserialized);
                    } catch (IllegalAccessException e) {
                        throw new YamlDeserializationException(e);
                    }
                }
            }
        }
    }

}
