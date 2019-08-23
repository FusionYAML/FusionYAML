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
import me.brokenearthdev.simpleyaml.entities.DefaultParser;
import me.brokenearthdev.simpleyaml.entities.YamlParser;
import me.brokenearthdev.simpleyaml.utils.YamlUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlObject implements YamlElement {

    public static final char SEPARATOR = '.';

    protected Map<String, YamlElement> map = new LinkedHashMap<>();

    public YamlObject() {}

    public YamlObject(Map<String, YamlElement> data) {
        map = data;
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
        Map<Object, Object> map = YamlUtils.toMap0(this);
        Map<Object, Object> newMap = YamlUtils.setNested(map, paths, value);
        this.map = YamlUtils.toMap(newMap);
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
