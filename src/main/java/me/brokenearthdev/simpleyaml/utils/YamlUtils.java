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

    @SuppressWarnings("unchecked")
    public static YamlElement toYamlElement(String key, Object o, boolean tree) {
        if (o instanceof Map && !tree)
            return new YamlNode(key, (Map) o /* fix */);
        else if (o instanceof Map)
            return toYamlObject(key, (Map<Object, Object>) o);
        else if (isPrimitive(o))
            return new YamlPrimitive(o);
        else if (o instanceof List)
            return new YamlList(toYamlElementList(key, (List) o));
        return null;
    }

    public static YamlObject toYamlObject(String key, Map<Object, Object> map) {
        Map<String, YamlElement> elementMap = new LinkedHashMap<>();
        map.forEach((k, v) -> elementMap.put(k.toString(), toYamlElement(key, v, true)));
        return new YamlObject(key, elementMap);
    }

    public static List<YamlElement> toYamlElementList(String key, List<Object> o) {
        List<YamlElement> elements = new LinkedList<>();
        o.forEach((v) -> elements.add(toYamlElement(key, v, false)));
        return elements;
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Number || o instanceof Character || o instanceof Boolean || o instanceof String;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> setNested(Map<String, Object> map, List<String> keys, Object value) {
        String key = keys.get(0);
        List<String> nextKeys = keys.subList(1, keys.size());
        if (nextKeys.size() == 0) {
            Map<String, Object> copyMap = new LinkedHashMap<>((Map) map);
            copyMap.put(key, value);
            return copyMap;
        } else {
            Map<String, Object> copyMap = new LinkedHashMap<>((Map) map);
            Map<String, Object> nextMap = (Map<String, Object>) map.get(key);
            if (!map.containsKey(key)) {
                nextMap.put(key, null);

            }
            Map<String, Object> map1 = setNested(nextMap, nextKeys, value);

            copyMap.put(key, map1);
            return copyMap;
        }
    }

}
