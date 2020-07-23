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

import io.github.fusionyaml.object.*;

import java.util.*;

/**
 * Class not intended for public usage
 */
public class YamlUtils {

    public static YamlElement toElement(Object o) {
        if (o == null) return null;
        if (o instanceof YamlElement)
            return (YamlElement) o;
        if (isPrimitive(o))
            return new YamlPrimitive(o);
        if (o instanceof List) {
            return toYamlList((List<Object>) o);
        }
        if (o instanceof Map) {
            return new YamlObject(toMap(toStrMap((Map) o)));
        }
        else return null;
    }

    public static <T> T toObject0(YamlElement e) {
        if (e instanceof YamlPrimitive)
            return (T) e.getAsYamlPrimitive().getValue();
        else if (e instanceof YamlArray)
            return (T) toObjList(e.getAsYamlList().getList());
        else if (e instanceof YamlObject)
            return (T) toMap0(e.getAsYamlObject().getMap());
        else return null;
    }

    public static <K, V> Map<String, Object> toStrMap(Map<K, V> map) {
        Map<String, Object> mp = new LinkedHashMap<>();
        map.forEach((k, v) -> {
            mp.put(k.toString(), v);
        });
        return mp;
    }

    public static Map<String, YamlElement> toMap(Map<String, Object> map) {
        Map<String, YamlElement> map1 = new LinkedHashMap<>();
        map.forEach((k, v) ->  {
            map1.put(k, toElement(v));
        });
         return map1;
    }

    public static <T> YamlArray toYamlList(Collection<T> o) {
        YamlArray list = new YamlArray();
        o.forEach(b -> {
            list.add(toElement(b));
        });
        return list;
    }

    public static List<Object> toObjList(Collection<YamlElement> list) {
        List<Object> l = new LinkedList<>();
        list.forEach(e -> l.add(toObject(e)));
        return l;
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
            return toObjList(((YamlArray) element).getList());
        else if (element.isYamlObject())
            return toMap0(element.getAsYamlObject().getMap());
        else return null;
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Number || o instanceof Character || o instanceof Boolean || o instanceof String;
    }

    public static Map<String, Object> toMap0(YamlObject object) {
        Map<String, Object> empty = new LinkedHashMap<>();
        object.getMap().forEach((k, v) -> empty.put(k, toObject(v)));
        return empty;
    }

    private static Map<Object, Object> toMap0(Map<String, YamlElement> map) {
        Map<Object, Object> empty = new LinkedHashMap<>();
        map.forEach((k, v) -> empty.put(k, toObject(v)));
        return empty;
    }

    public static Map<String, Object> setNested(Map<String, Object> map, List<String> keys, Object value) {
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
            newValue = setNested((Map<String, Object>) v, nextKeys, value);
        }
        Map<String, Object> copyMap = new LinkedHashMap<>(map);
        if (newValue == null)
            copyMap.remove(key);
        else copyMap.put(key, newValue);
        return copyMap;
    }

    public static Object getObject(Map<?, ?> init, List<String> paths, Map newMap, String currentPath, boolean first, int loops) {
        if (paths.size() == 1)
            return init.get(paths.get(0));
        Map object = (first) ? init : newMap;
        if (object == null) return null;
        if (currentPath.equals(paths.get(paths.size() - 1))) {
            Object o = object.get(currentPath);
            return o;
        }
        for (Object o : object.keySet()) {
            if (!o.equals(currentPath)) continue;
            if (object.get(o) instanceof Map) {
                Map objMap = (Map) object.get(o);
                return getObject(init, paths, objMap, paths.get(loops + 1), false, loops + 1);
            }
        }
        return null;
    }

    public static boolean mapConstructorException(Exception e) {
        return e.getMessage().startsWith("Can't construct a java object for tag:yaml.org,2002:java.util.Map; " +
                "exception=No suitable constructor with 3 arguments found for interface java.util.Map");
    }


}
