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
package me.brokenearthdev.simpleyaml.object;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.brokenearthdev.simpleyaml.utils.YamlUtils;
import net.moltenjson.utils.Gsons;
import net.moltenjson.utils.ReflectiveTypes;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlObject implements YamlElement {

    private static final Gson gson = new Gson();

    protected Map<String, YamlElement> map = new LinkedHashMap<>();

    public YamlObject() {}

    public YamlObject(Map<String, YamlElement> data) {
        map = data;
    }

    public static YamlObject fromJsonObject(JsonObject object) {
        return new YamlObject(YamlUtils.toMap(Gsons.DEFAULT.fromJson(object, ReflectiveTypes.MAP_TYPE)));
    }

    public static YamlObject readFromJson(String json) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> jmap = gson.fromJson(json, Map.class);
        return new YamlObject(YamlUtils.toMap(jmap));
    }

    private void change(String key, YamlElement value) {
        if (value == null)
            map.remove(key);
        else map.put(key, value);
    }

    public void set(String key, YamlElement value) {
        change(key, value);
    }

    public void set(String key, String value) {
        change(key, createElementPrimitive(value));
    }

    public void set(String key, boolean value) {
        change(key, createElementPrimitive(value));
    }

    public void set(String key, Number value) {
        change(key, createElementPrimitive(value));
    }

    public void set(List<String> paths, YamlElement value) {
        if (paths.size() == 0) return; // empty path
        if (paths.size() == 1) {
            set(paths.get(0), value);
            return;
        }
        // path is nested
        Map<String, Object> map = YamlUtils.toMap0(this);
        Map<String, Object> newMap = YamlUtils.setNested(map, paths, value);
        this.map = YamlUtils.toMap(YamlUtils.toStrMap(newMap));
    }

    public JsonObject saveToJsonObject() {
        return gson.fromJson(toJson(), JsonObject.class);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public Map<String, YamlElement> getMap() {
        return map;
    }

    private YamlElement createElementPrimitive(Object o) {
        return new YamlPrimitive(o);
    }

    @Override
    public String toString() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        return yaml.dump(YamlUtils.toMap0(this));
    }

    public String toYaml() {
        return toString();
    }

    public String toJson() {
        return new Gson().toJson(YamlUtils.toMap0(this));
    }

}
