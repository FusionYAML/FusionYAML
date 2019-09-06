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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class StorageUtils {

    public static <E> ImmutableList<E> add(ImmutableList<E> list, E... items) {
        if (items == null || items.length == 0)
            return list;
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        for (E element : list)
            builder.add(element);
        for (E item : items)
            builder.add(items);
        return builder.build();
    }

    public static <E> ImmutableList<E> remove(ImmutableList<E> list, E... items) {
        if (items == null || items.length == 0)
            return list;
        List<E> newList = new ArrayList<>(list);
        for (E item : items) {
            newList.remove(item);
        }
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        newList.forEach(builder::add);
        return builder.build();
    }

    public static <K,V> ImmutableMap<K,V> toImmutableMap(Map<K,V> map) {
        ImmutableMap.Builder<K,V> builder = new ImmutableMap.Builder<>();
        for (K k : map.keySet())
            builder.put(k, map.get(k));
        return builder.build();
    }

    public static <K,V> ImmutableMap<K,V> addToImmutableMap(ImmutableMap<K,V> map, K key, V value) {
        Map<K,V> map1 = new HashMap<>();
        map.forEach(map1::put);
        map1.put(key, value);
        ImmutableMap.Builder<K,V> builder = new ImmutableMap.Builder<>();
        map1.forEach(builder::put);
        return builder.build();
    }

    public static <E> ImmutableList<E> toImmutableList(List<E> e) {
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        e.forEach(builder::add);
        return builder.build();
    }

    public static List<String> toList(String path, char separator) {
        List<String> paths = new LinkedList<>();
        char[] chars = path.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == separator || i + 1 == chars.length) {
                if (i + 1 == chars.length)
                    str.append(chars[i]);
                paths.add(str.toString());
                str = new StringBuilder();
                continue;
            }
            str.append(chars[i]);
        }
        return paths;
    }

    public static <T> List<Map> toList(Map<String, T> map) {
        List<Map> list = new LinkedList<>();
        map.forEach((k, v) -> {
            Map<String, T> map2 = new LinkedHashMap<>();
            map2.put(k, v);
            list.add(map2);
        });
        return list;
    }

}
