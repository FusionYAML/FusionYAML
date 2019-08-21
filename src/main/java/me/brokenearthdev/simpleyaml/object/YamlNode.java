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

import me.brokenearthdev.simpleyaml.struct.Node;
import me.brokenearthdev.simpleyaml.utils.StorageUtils;

import java.util.List;
import java.util.Map;

public class YamlNode extends Node implements YamlElement {

    public YamlNode(String path, char separator, Map map) {
        super(path, separator, map);
    }

    @Override
    @SuppressWarnings("unchecked") // Map is somewhat similar to Map<Object, Object>
    public void addChild(String key, Object value) {
        children.put(key, value);
        for (Object o : data.keySet()) {
            //if (o.toString().equals())
        }
    }

    @Override
    public void removeChild(String key, Object value) {
        children.remove(key);
    }



}
