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
package me.brokenearthdev.simpleyaml.utils;

import me.brokenearthdev.simpleyaml.object.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class YamlUtils {

    public static YamlElement toElement(Object o, boolean tree) {
        if (o == null) return null;
        if (o instanceof YamlElement)
            return (YamlElement) o;
        if (isPrimitive(o))
            return new YamlPrimitive(o);
        if (o instanceof List)
            return toYamlList((List<Object>) o);
        if (o instanceof Map && tree)
            return new YamlObject(toMap((Map<Object, Object>) o));
        if (o instanceof Map)
            return new YamlNode(toMap((Map<Object, Object>) o));
        else return null;
    }

    public static Map<String, YamlElement> toMap(Map<Object, Object> map) {
        Map<String, YamlElement> map1 = new LinkedHashMap<>();
        map.forEach((k, v) -> map1.put(k.toString(), toElement(v, false)));
        return map1;
    }

    public static YamlList toYamlList(List<Object> o) {
        YamlList list = new YamlList();
        o.forEach(b -> list.add(toElement(b, false)));
        return list;
    }

    private static Object toObject(YamlElement element) {
        if (element.isYamlPrimitive()) {
            YamlPrimitive primitive = element.getAsYamlPrimitive();
            if (primitive.isNumber())
                return primitive.getAsNumber();
            else if (primitive.isCharacter())
                return primitive.getAsChar();
            else if (primitive.isBoolean())
                return primitive.getAsBoolean();
            else
                return primitive.getAsString();
        }
        else if (element.isYamlList())
            return ((YamlList) element).getList();
        else if (element.isYamlNode()) {
            return toMap0(element.getAsYamlNode().getChildren());
        }
        else if (element.isYamlObject())
            return toMap0(element.getAsYamlObject().getMap());
        else return null;
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Number || o instanceof Character || o instanceof Boolean || o instanceof String;
    }

    public static Map<Object, Object> toMap0(YamlObject object) {
        Map<Object, Object> empty = new LinkedHashMap<>();
        object.getMap().forEach((k, v) -> empty.put(k, toObject(v)));
        return empty;
    }

    private static Map<Object, Object> toMap0(Map<String, YamlElement> map) {
        Map<Object, Object> empty = new LinkedHashMap<>();
        map.forEach((k, v) -> empty.put(k, (v instanceof YamlNode) ? toMap0(((YamlNode) v).getChildren()) : toObject(v)));
        return empty;
    }

    public static Map<Object, Object> setNested(Map<Object, Object> map, List<String> keys, Object value) {
        String key = keys.get(0);
        List<String> nextKeys = keys.subList(1, keys.size());
        Object newValue;
        if (nextKeys.size() == 0) {
            newValue = value;
        } else if (!map.containsKey(key)) {
            newValue = setNested(new LinkedHashMap<>(), nextKeys, value);
        } else {
            Object v = map.get(key);
            if (!(v instanceof Map))
                v = new LinkedHashMap<>();
            newValue = setNested((Map<Object, Object>) v, nextKeys, value);
        }
        Map<Object, Object> copyMap = new LinkedHashMap<>(map);
        copyMap.put(key, newValue);
        return copyMap;
    }





}
