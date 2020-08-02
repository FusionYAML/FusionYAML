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
package com.github.fusionyaml.object;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class represents a list structure under a given path in {@code yaml}.
 * Its data can be retrieved by {@link #getList()} and modified by
 * {@link #add(YamlElement)} and {@link #remove(int)}
 */
public class YamlArray implements YamlElement, Iterable<YamlElement> {

    /**
     * The {@link List} of {@link YamlElement}s
     */
    private Collection<YamlElement> list = new ArrayList<>();

    /**
     * This constructor requires no objects to be passed into their parameters. An
     * empty {@link LinkedList} is created upon initialization.
     */
    public YamlArray() {
    }

    public YamlArray(int capacity) {
        this.list = new ArrayList<>(capacity);
    }

    /**
     * This constructor requires a {@link List} of {@link YamlElement}s to be
     * passed into the constructor. The {@link Collection} containing {@link YamlElement}s
     * in this object will be set equal to the value passed into the constructor.
     * <p>
     * It is encouraged to pass in a {@link LinkedList} object for predictable iteration
     * order.
     *
     * @param list The {@link List} of {@link YamlElement}s
     */
    public YamlArray(List<YamlElement> list) {
        this((Collection<YamlElement>) list);
    }

    /**
     * This constructor requires a {@link Collection} of {@link YamlElement}s to be
     * passed into the constructor. The {@link Collection} containing {@link YamlElement}s
     * in this object will be set equal to the value passed into the constructor.
     * <p>
     * It is encouraged to pass in a {@link LinkedList} object for predictable iteration
     * order.
     *
     * @param list The {@link Collection} of {@link YamlElement}s
     */
    public YamlArray(Collection<YamlElement> list) {
        this.list = list;
    }

    /**
     * Adds a {@link YamlElement} entry into the {@link Collection}.
     *
     * @param value The {@link YamlElement} value
     */
    public void add(@NotNull YamlElement value) {
        list.add(value);
    }

    /**
     * Adds a {@code boolean} entry into the {@link Collection}.
     *
     * @param value The {@code boolean} value
     */
    public void add(boolean value) {
        list.add(new YamlPrimitive(value));
    }

    /**
     * Adds a {@link String} entry into the {@link Collection}.
     *
     * @param value The {@link String} value
     */
    public void add(@NotNull String value) {
        list.add(new YamlPrimitive(value));
    }

    /**
     * Adds a {@link String} entry into the {@link Collection}.
     *
     * @param number The {@link String} value
     */
    public void add(@NotNull Number number) {
        list.add(new YamlPrimitive(number));
    }

    /**
     * Removes a {@link YamlElement} entry in a given index.
     *
     * @param index The index where the {@link YamlElement} entry
     *              is found.
     */
    public void remove(int index) {
        if (list instanceof List)
            ((List) list).remove(index);
        else {
            LinkedList<YamlElement> aList = new LinkedList<>(list);
            aList.remove(index);
            list = aList;
        }
    }

    /**
     * @return The {@link Collection} of {@link YamlElement}s
     */
    public Collection<YamlElement> getList() {
        return list;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<YamlElement> iterator() {
        return list.iterator();
    }
}
