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


import me.brokenearthdev.simpleyaml.entities.DefaultParser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlObject implements YamlElement {

    protected Map<String, YamlElement> map = new LinkedHashMap<>();
    private String upperMost;

    public YamlObject(String upperMost) {
        this.upperMost = upperMost;
    }

    public YamlObject(String upperMost, Map<String, YamlElement> children) {
        this.upperMost = upperMost;
        map = children;
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

    public void set(String key, char separator, String value) {

    }

    private void setNested0(List<String> paths, Object value) {
        //Map<Object, Object>
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



}
