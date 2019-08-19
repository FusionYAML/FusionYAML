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
package me.brokenearthdev.simpleyaml.tree;

import me.brokenearthdev.simpleyaml.entities.YamlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

    private List<YamlElement> elements = new ArrayList<>();
    private Map<Object, Object> map = new HashMap<>();

    public Node() {}
    public Node(List<YamlElement> children) {
        this.elements = children;
        this.map = (children.size() > 0) ? children.get(0).getAsMap() : new HashMap<>();
    }

    public void addChild(YamlElement element) {
        elements.add(element);

    }

}
