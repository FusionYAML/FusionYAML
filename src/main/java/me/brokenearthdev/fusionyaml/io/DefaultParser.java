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
package me.brokenearthdev.fusionyaml.io;

import com.google.gson.Gson;
import me.brokenearthdev.fusionyaml.error.YamlException;
import me.brokenearthdev.fusionyaml.object.*;
import me.brokenearthdev.fusionyaml.utils.StorageUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Built-in parser
 */
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

    public DefaultParser(@NotNull Map<String, Object> map) {
        super(map);
    }

    @Override
    public @Nullable Map<String, Object> map() throws YamlException {
        try {
            if (raw == null)
                return data;
            data = new Yaml().loadAs(raw, Map.class);
            return data;
        } catch (Exception e) {
            if (e.getCause().toString().equals("org.yaml.snakeyaml.error.YAMLException: No suitable constructor with 1 arguments found for interface java.util.Map"))
                throw new UnsupportedOperationException("Lists in the uppermost tree are not supported. You can only set lists under a parent or a key");
            throw new YamlException("Invalid YAML", e.getCause());
        }
    }

    @Override
    public @NotNull String toJson() {
        Gson gson = new Gson();
        return gson.toJson(raw);
    }

    @Override
    public @Nullable Object getObject(@NotNull String path, char dirSeparator) {
        if (data == null)
            return null;
        if (path.startsWith(".") || path.endsWith("."))
            return null;
        List<String> paths = StorageUtils.toList(path, dirSeparator);
        return getObject(paths);
    }

    @Override
    public @Nullable Object getObject(@NotNull List<String> path) {
        if (data == null)
            return null;
        if (path.size() == 0)
            return null;
        return getObject(data, path, new HashMap(), path.get(0), true, 0);
    }

    @Override
    public @Nullable Object getObject(@NotNull String[] path) {
        if (data == null)
            return null;
        return getObject(new LinkedList<>(Arrays.asList(path)));
    }

    @Override
    public @Nullable YamlElement getElement(@NotNull String path, char dirSeparator) {
        return getElement(StorageUtils.toList(path, dirSeparator));
    }

    @Override
    public @Nullable YamlElement getElement(@NotNull List<String> path) {
        if (path.size() == 0) return null;
        Object o = getObject(path);
        return YamlUtils.toElement(o, path.size() == 1 && !(o instanceof Map));
    }

    @Override
    public @Nullable YamlElement getElement(@NotNull String[] path) {
        return getElement(new LinkedList<>(Arrays.asList(path)));
    }

    private static Object getObject(Map<?, ?> init, List<String> paths, Map newMap, String currentPath, boolean first, int loops) {
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

}