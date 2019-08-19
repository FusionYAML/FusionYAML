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
package me.brokenearthdev.simpleyaml.entities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DefaultParser extends YamlParser {

    public DefaultParser(@NotNull String raw) {
        super(raw);
    }

    public DefaultParser(@NotNull File file) throws IOException {
        super(file);
    }

    public DefaultParser(@NotNull URL url) throws IOException {
        super(url);
    }

    @Override
    public @Nullable Map<?, ?> map() {
        try {
            map = new Yaml().loadAs(raw, Map.class);
            return map;
        } catch (Exception e) {
            throw new YAMLException("Invalid YAML", e.getCause());
        }
    }

    @Override
    public @NotNull String toJson() {
        Gson gson = new Gson();
        return gson.toJson(raw);
    }

    @Override
    public @Nullable Object getObject(@NotNull String path, char dirSeparator) {
        if (map == null)
            map = map();
        if (path.startsWith(".") || path.endsWith("."))
            return null;
        List<String> paths = new LinkedList<>();
        char[] chars = path.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == dirSeparator || i + 1 == chars.length) {
                if (i + 1 == chars.length)
                    str.append(chars[i]);
                paths.add(str.toString());
                str = new StringBuilder();
                continue;
            }
            str.append(chars[i]);
        }
        return getObject(paths);
    }

    @Override
    public @Nullable Object getObject(@NotNull List<String> path) {
        if (map == null)
            map = map();
        if (path.size() == 0)
            return null;
        return getObject(map, path, new HashMap(), path.get(0), true, 0);
    }

    @Override
    public @Nullable Object getObject(@NotNull String[] path) {
        if (map == null)
            map = map();
        return getObject(new LinkedList<>(Arrays.asList(path)));
    }

    private Object getObject(Map<?, ?> mapped, List<String> paths, Map newMap, String currentPath, boolean first, int loops) {
        Map map = (first) ? mapped : newMap;
        if (map == null) return null;
        if (currentPath.equals(paths.get(paths.size() - 1))) return map.get(currentPath);
        for (Object o : map.keySet()) {
            if (!o.equals(currentPath)) continue;
            if (map.get(o) instanceof Map) {
                Map objMap = (Map) map.get(o);
                return getObject(mapped, paths, objMap, paths.get(loops + 1), false, loops + 1);
            }
        }
        return null;
    }
}
