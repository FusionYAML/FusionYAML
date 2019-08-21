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

import me.brokenearthdev.simpleyaml.object.YamlElement;
import me.brokenearthdev.simpleyaml.utils.StorageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// also called a node
public abstract class TreeSection {

    private Map<Object, Object> map = new HashMap<>();
    private Map<Object, Object> tree = new HashMap<>();
    private List<String> paths = new ArrayList<>();
    private List<YamlElement> elements = new ArrayList<>();

    public TreeSection(List<String> paths, Map<Object, Object> tree) {
        this.paths = paths;
        this.tree = tree;
    }

    public TreeSection(String path, char separator, Map<Object, Object> tree) {
        this(StorageUtils.toList(path, separator), tree);
    }

    public abstract boolean set(List<String> paths, Object o);
    protected abstract boolean remove(List<String> paths, Object o);

    public boolean set(String path, char separator, Object o) {
        return set(StorageUtils.toList(path, separator), o);
    }

    public boolean remove(String path, char separator, Object o) {
        return remove(StorageUtils.toList(path, separator), o);
    }

    public abstract TreeSection getTreeSection();



}
