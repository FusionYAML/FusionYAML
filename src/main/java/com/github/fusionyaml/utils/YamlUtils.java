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
package com.github.fusionyaml.utils;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.object.YamlObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class not intended for public usage
 */
public class YamlUtils {

    public static Map<String, Object> toMap0(YamlObject object) {
        Map<String, Object> empty = new LinkedHashMap<>();
        object.forEach((k, v) -> empty.put(k, $DataBridge.toObject(v)));
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

    public static <V> Map<String, V> setNested0(Map<String, V> map, List<String> keys, Object value) {
        String key = keys.get(0);
        List<String> nextKeys = keys.subList(1, keys.size());
        Object newValue;
        if (nextKeys.size() == 0) {
            newValue = value;
        } else if (!map.containsKey(key)) {
            newValue = setNested0(new LinkedHashMap<>(), nextKeys, value);
        } else {
            Object v = map.get(key);
            if (!(v instanceof Map))
                v = new LinkedHashMap<>();
            newValue = setNested0((Map<String, V>) v, nextKeys, value);
        }
        Map<String, V> copyMap = new LinkedHashMap<>(map);
        if (newValue == null)
            copyMap.remove(key);
        else copyMap.put(key, (V) newValue);

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

    public static Object getObjectInYamlObject(YamlObject init, List<String> paths, YamlObject newMap, String currentPath, boolean first, int loops) {
        if (paths.size() == 1)
            return init.get(paths.get(0));
        YamlObject object = (first) ? init : newMap;
        if (object == null) return null;
        if (currentPath.equals(paths.get(paths.size() - 1))) {
            Object o = object.get(currentPath);
            return o;
        }
        for (Object o : object.keySet()) {
            if (!o.equals(currentPath)) continue;
            if (object.get(o.toString()) instanceof YamlObject) {
                YamlObject objMap = (YamlObject) object.get(o.toString());
                return getObjectInYamlObject(init, paths, objMap, paths.get(loops + 1), false, loops + 1);
            }
        }
        return null;
    }

    public static boolean mapConstructorException(Exception e) {
        return e.getMessage().startsWith("Can't construct a java object for tag:yaml.org,2002:java.util.Map; " +
                "exception=No suitable constructor with 3 arguments found for interface java.util.Map");
    }


}
