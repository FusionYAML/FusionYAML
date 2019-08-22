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

import me.brokenearthdev.simpleyaml.object.YamlElement;
import me.brokenearthdev.simpleyaml.object.YamlObject;
import me.brokenearthdev.simpleyaml.object.YamlPrimitive;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlNode implements YamlElement {

    // children

    private Map<String, YamlElement> map = new LinkedHashMap<>();
    private String str;

    public YamlNode(String name) {
        str = name;
    }

    public YamlNode(String name, Map<String, YamlElement> children) {
        this.map = children;
        str = name;
    }

    public Map<String, YamlElement> getChildren() {
        return map;
    }

    public String getKeyName() {
        return str;
    }

    public void addChild(String key, YamlElement element) {
        change(key, element);
    }

    public void addChild(String key, String value) {
        change(key, createElementPrimitive(value));
    }

    public void addChild(String key, boolean value) {
        change(key, createElementPrimitive(value));
    }

    public void addChild(String key, Number value) {
        change(key, createElementPrimitive(value));
    }

    private void change(String key, YamlElement element) {
        if (element == null)
            map.remove(key);
        else
            map.put(key, element);
    }

    private YamlElement createElementPrimitive(Object o) {
        return new YamlPrimitive(o);
    }

}
