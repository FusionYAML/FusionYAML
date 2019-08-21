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


import java.util.LinkedHashMap;
import java.util.Map;

public class YamlObject implements YamlElement {

        private final LinkedHashMap<String, YamlElement> map = new LinkedHashMap<>();

    public void add(String key, YamlElement value) {
        value = (value == null) ? YamlNull.INSTANCE : value;
        map.put(key, value);
    }

    public void add(String key, String value) {
        map.put(key, createElementPrimitive(value));
    }

    public void add(String key, boolean value) {
        map.put(key, createElementPrimitive(value));
    }

    public void add(String key, Number value) {
        map.put(key, createElementPrimitive(value));
    }

    public Map<String, YamlElement> getMap() {
        return map;
    }

    private YamlElement createElementPrimitive(Object o) {
        return o == null ? YamlNull.INSTANCE : new YamlPrimitive(o);
    }

}
