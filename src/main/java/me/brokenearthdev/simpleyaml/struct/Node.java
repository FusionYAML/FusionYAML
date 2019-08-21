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
package me.brokenearthdev.simpleyaml.struct;

import me.brokenearthdev.simpleyaml.entities.DefaultParser;
import me.brokenearthdev.simpleyaml.entities.YamlParser;
import me.brokenearthdev.simpleyaml.utils.StorageUtils;

import java.util.*;

public abstract class Node {

    // data map
    protected Map data;
    protected Map children = new HashMap();

    protected List<String> paths = new LinkedList<>();

    public Node(String path, char separator, Map map) {
        this.data = map;
        YamlParser parser = new DefaultParser(map);
        Object retrieved = parser.getObject(path, separator);
        if (retrieved instanceof Map)
            this.children = (Map) retrieved;
        if (retrieved instanceof Node)
            this.children = ((Node) retrieved).children;
        this.paths = StorageUtils.toList(path, separator);
    }

    public abstract void addChild(String key, Object value);
    public abstract void removeChild(String key, Object value);

    public Map<?, ?> getChildren() {
        return children;
    }

    public Map<?, ?> getYamlAsMap() {
        return data;
    }

}
