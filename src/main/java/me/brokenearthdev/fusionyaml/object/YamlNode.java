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
package me.brokenearthdev.fusionyaml.object;

import com.google.common.collect.ImmutableList;
import me.brokenearthdev.fusionyaml.utils.StorageUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class YamlNode implements YamlElement {

    // children

    private Map<String, YamlElement> map = new LinkedHashMap<>();
    private List<YamlNode> childNodes = new LinkedList<>();

    public YamlNode() {
    }

    private List<YamlNode> findChildNodes(Map<String, YamlElement> map) {
        List<YamlNode> list = new LinkedList<>();
        map.forEach((k, v) -> {
            if (v instanceof YamlNode)
                list.add((YamlNode) v);
        });
        return list;
    }

    public YamlNode(Map<String, YamlElement> children) {
        this.map = children;
        this.childNodes = findChildNodes(children);
    }

    public Map<String, YamlElement> getChildren() {
        return map;
    }

    public void addChild(String key, YamlElement element) {
        if (element instanceof YamlNode)
            childNodes.add((YamlNode) element);
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

    public ImmutableList<YamlNode> getChildNodes() {
        return StorageUtils.toImmutableList(childNodes);
    }

}
