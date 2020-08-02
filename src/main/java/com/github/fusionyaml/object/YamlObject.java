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


import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.configurations.Configuration;
import com.github.fusionyaml.configurations.YamlConfiguration;
import com.github.fusionyaml.events.EntryChangeListener;
import com.github.fusionyaml.utils.YamlUtils;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * A {@code YamlObject} stores {@code YAML} key-value pairs in a {@link java.util.LinkedHashMap}
 * ready to be converted into {@code JSON} and a {@code YAML} {@link String}.
 * <p>
 * A {@link YamlObject} can be thought of key-value pairs that can be retrieved, modified, and
 * stored.
 */
public class YamlObject implements YamlElement {

    /**
     * The {@link EntryChangeListener} for this object
     */
    private EntryChangeListener listener;

    /**
     * A {@link LinkedHashMap} containing key-value pairs which essentially are where
     * all the {@code YAML} data are stored.
     */
    protected Map<String, YamlElement> map = new LinkedHashMap<>();

    /**
     * The {@link Configuration} for this {@link YamlObject}
     */
    private final Configuration configuration;

    /**
     * This constructor requires no objects to be passed into their parameters. An
     * empty {@link LinkedHashMap} and a {@link FusionYAML} object is created upon
     * initialization.
     */
    public YamlObject() {
        configuration = new YamlConfiguration(this);
    }

    /**
     * A method that sets the value in a given key. A value is removed from a
     * key if the value passed in is {@code null}
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     */
    private void change(@NotNull String key, YamlElement value) {
        map.put(key, value == null ? YamlNull.NULL : value);
        if (listener != null)
            listener.onChange(this, Collections.singletonList(key), value);
    }

    /**
     * Sets a {@link YamlElement} value in a given key. If the value is {@code null},
     * the key with its value will be removed.
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     * @return this object
     */
    public YamlObject set(@NotNull String key, YamlElement value) {
        change(key, value);
        return this;
    }

    /**
     * Sets a {@link String} value in a given key. If the value is {@code null},
     * the key with its value will be removed.
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     * @return this object
     */
    public YamlObject set(@NotNull String key, String value) {
        change(key, createElementPrimitive(value));
        return this;
    }

    /**
     * Sets a {@link Boolean} value in a given key. If the value is {@code null},
     * the key with its value will be removed.
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     * @return this object
     */
    public YamlObject set(@NotNull String key, Boolean value) {
        change(key, createElementPrimitive(value));
        return this;
    }

    /**
     * Sets a {@link Number} value in a given key. If the value is {@code null},
     * the key with its value will be removed.
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     * @return this object
     */
    public YamlObject set(@NotNull String key, Number value) {
        change(key, createElementPrimitive(value));
        return this;
    }

    /**
     * Sets the {@link YamlElement} value in the given path, which is expressed as a {@link List}.
     * Each index in a list is a descent under its parent, which is occasionally the
     * {@link String} in the previous index except the {@link String} at index zero,
     * which is the uppermost parent.
     * <p>
     * If the value is set to {@code null}, the key and its value will be removed.
     *
     * @param paths The path to the value.
     * @param value The value the path holds
     * @return this object
     */
    public YamlObject set(@NotNull List<String> paths, YamlElement value) {
        if (paths.size() == 0) return this; // empty path
        if (listener != null)
            listener.onChange(this, paths, value);
        if (paths.size() == 1) {
            set(paths.get(0), value);
            return this;
        }
        this.map = YamlUtils.setNested0(map, paths, value);
        return this;
    }

    public YamlObject set(String path, char separator, YamlElement value) {
        return this.set(Splitter.on(separator).splitToList(path), value);
    }


    /**
     * Removes the {@link YamlElement} value in the given path, which is expressed as a {@link List}.
     * Each index in a list is a descent under its parent, which is occasionally the
     * {@link String} in the previous index except the {@link String} at index zero,
     * which is the uppermost parent.
     * <p>
     * Calling this method is equivalent to calling {@link #set(List, YamlElement)} with {@code null}
     * passed in as the second parameter.
     *
     * @param paths The path to the value.
     * @return this object
     */
    public YamlObject remove(@NotNull List<String> paths) {
        map = YamlUtils.setNested0(map, paths, null);
        return this;
    }

    /**
     * Removes the value in a given path. Calling this method is equivalent to calling
     * {@link #set(String, YamlElement)} with {@code null} passed in as the second
     * parameter.
     *
     * @param key The key for the value
     * @return this object
     */
    public YamlObject remove(@NotNull String key) {
        map.remove(key);
        return this;
    }

    /**
     * Accessing this method from a {@link Configuration} will never return the same {@link Configuration}
     * object. The object returned is a {@link YamlConfiguration}.
     *
     * @return The configuration for this {@link YamlObject}.
     */
    public Configuration toConfiguration() {
        return configuration;
    }

    /**
     * Creates a {@link YamlPrimitive} object from the given {@link Object}. If the
     * object is not of a primitive type and a {@link String}, an {@link IllegalArgumentException}
     * will be thrown.
     *
     * @param o The required {@link Object} that is of primitive type or a {@link String}
     * @return A {@link YamlPrimitive} object created from the {@link Object} provided or
     * {@code null} if the object provided is {@code null}
     */
    private YamlElement createElementPrimitive(Object o) {
        if (o == null)
            return YamlNull.NULL;
        return new YamlPrimitive(o);
    }

    public YamlElement get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public void forEach(BiConsumer<? super String, ? super YamlElement> action) {
        for (String key : keySet()) {
            YamlElement value = get(key);
            action.accept(key, value);
        }
    }

    public int size() {
        return map.size();
    }

    public YamlElement get(int index) {
        return map.get(new LinkedList<>(keySet()).get(index));
    }

    /**
     * Sets the {@link EntryChangeListener} for the object. When an entry changed by adding,
     * removing, or modifying data, {@link EntryChangeListener#onChange(YamlObject, List, Object)}
     * will be called.
     *
     * @param listener The {@link EntryChangeListener}
     */
    public void setOnEntryChange(EntryChangeListener listener) {
        this.listener = listener;
    }

}
