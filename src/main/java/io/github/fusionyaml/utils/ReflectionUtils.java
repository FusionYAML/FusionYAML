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
package io.github.fusionyaml.utils;

import com.google.common.reflect.TypeToken;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.adapters.ObjectTypeAdapter;
import net.moltenjson.configuration.select.SelectionHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Class not intended for public usage
 */
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
        if (map.size() != fields.size()) {
            return false;
        }
        for (Field field : fields) {
            if (!map.containsKey(field.getName()))
                return false;
        }
        return true;
    }

    public static boolean contains(Class c1, Class c2) {
        return c1.equals(c2) || c1.getSuperclass() != null && contains(c1.getSuperclass(), c2) || containsInterface(c1, c2);
    }

    public static int lps(Class c1, Class c2, int lps) {
        if (c1.equals(c2)) return lps;
        if (c1.getSuperclass() != null) {
            int contains = lps(c1.getSuperclass(), c2, lps + 1);
            int containsInt = lpsInt(c1, c2, lps + 1);
            return (containsInt == -1) ? contains : (contains == -1) ? containsInt : Math.min(contains, containsInt);
        }
        else return -1;
    }

    public static int lpsInt(Class c1, Class c2, int lps) {
        Class[] interfaces = c1.getInterfaces();
        for (Class clazz : interfaces) {
            int lpsInt = lpsInt(clazz, c2, lps + 1);
            if (clazz.equals(c2) || (lpsInt != -1))
                return (clazz.equals(c2)) ? lps : lpsInt;
        }
        return -1;
    }

    private static boolean containsInterface(Class c1, Class c2) {
        Class[] interfaces = c1.getInterfaces();
        for (Class clazz : interfaces) {
            if (clazz.equals(c2) || containsInterface(clazz, c2))
                return true;
        }
        return false;
    }

    public static <T> Class<?> getTypeFor(T o) {
        if (o instanceof Collection)
            return LinkedList.class;
        else if (o instanceof Map)
            return LinkedHashMap.class;
        else return o.getClass();
    }

    public static void assignFields(Object o, Map map, List<Field> fields, ObjectTypeAdapter objDeserializer, Type type) throws YamlDeserializationException {
        for (Object key : map.keySet()) {
            for (Field field : fields) {
                if (key.toString().equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        Object found = map.get(field.getName());
                        Object deserialized = objDeserializer.deserialize(YamlUtils.toElement(found, false), type);
                        if (deserialized == null) continue;
                        if (deserialized instanceof Map)
                            deserialized = objDeserializer.deserialize(YamlUtils.toElement(deserialized, true), field.getType());
                        field.set(o, deserialized);
                    } catch (IllegalAccessException e) {
                        throw new YamlDeserializationException(e);
                    }
                }
            }
        }
    }

    public static Type getGeneric(Field field) {
        field.setAccessible(true);
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return TypeToken.of(type).resolveType(SelectionHolder.class.getTypeParameters()[0]).getType();
    }

}
