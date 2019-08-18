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
package me.brokenearthdev.simpleyaml.entities;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.HashMap;
import java.util.Map;

/**
 * YamlElement is an immutable class that houses key-value pair(s). A value in this class can be a map that also
 * houses key-value pair(s), which can also house key-value pair(s), and so on. This kind of data arrangement
 * is known as tree.
 * <p>
 * The key in this class is the highest in the hierarchy. There can be only one outermost key.
 */
public class YamlElement {

    /**
     * The key, which is the identifier for the value
     */
    private String key;

    /**
     * The value that its key holds
     */
    private Object value;

    /**
     * Key-value pairs written in yaml syntax
     */
    private String yaml;

    /**
     * Key-value pairs stored in a map
     */
    private Map<Object, Object> map;

    /**
     * This constructor requires a key-value pair to be passed into its parameters where
     * the key is the highest in the hierarchy containing a value. The value may contain
     * nodes.
     * <p>
     * Using separators to represent a decent in hierarchy will not work. Instead, pass in
     * a map as the value.
     *
     * @param key The key that contains a value
     * @param value The value that its key contains
     * @param options A {@link DumperOptions} instance which contains convenient options t
     *                hat adjust styles in yaml to meet your preferences
     */
    public YamlElement(@NotNull String key, @NotNull Object value, DumperOptions options) {
        Yaml yaml = (options != null) ? new Yaml(options) : new Yaml();
        this.key = key;
        this.value = value;
        this.map = new HashMap<>();
        this.map.put(key, value);
        this.yaml = yaml.dump(map);
    }

    /**
     * This constructor requires a key-value pair to be passed into its parameters where
     * the key is the highest in the hierarchy containing a value. The value may contain
     * nodes.
     * <p>
     * Using separators to represent a decent in hierarchy will not work. Instead, pass in
     * a map as the value.
     *
     * @param key The key that contains a value
     * @param value The value that its key contains
     */
    public YamlElement(@NotNull String key, @NotNull Object value) {
        this(key, value, null);
    }

    /**
     * This constructor requires a map that contains data. The map will be converted to {@code YAML}
     * upon initialization. Please note that the size of the map passed in should never exceed or be
     * less than {@code 1}
     * <p>
     * The value in the map can also contain another map, creating a tree. Using separators to represent
     * a descent in hierarchy will not work.
     *
     * @param map The map that contains key-value information
     * @param options A {@link DumperOptions} instance which contains convenient options that adjust
     *                styles in yaml to meet your preferences
     */
    public YamlElement(@NotNull Map<Object, Object> map, DumperOptions options) {
        if (map.size() != 1)
            throw new YAMLException("The size of the map passed in is not equal to one");
        for (Map.Entry entry : map.entrySet()) {
            this.key = entry.getKey().toString();
            this.value = entry.getValue();
        }
        if (key == null || value == null)
            throw new YAMLException("One of the objects in the map is null");
        this.map = map;
        Yaml yaml = (options != null) ? new Yaml(options) : new Yaml();
        this.yaml = yaml.dump(map);
    }

    /**
     * This constructor requires a map that contains data. The map will be converted to {@code YAML}
     * upon initialization. Please note that the size of the map passed in should never exceed or be
     * less than {@code 1}
     * <p>
     * The value in the map can also contain another map, creating a tree. Using separators to represent
     * a descent in hierarchy will not work.
     *
     * @param map The map that contains key-value information
     */
    public YamlElement(@NotNull Map<Object, Object> map) {
        this(map, null);
    }

    /**
     * A key contains a value, retrieved by calling {@link #getValue()}
     *
     * @return The key that contains a value.
     */
    public String getKey() {
        return key;
    }

    /**
     * A value is the data that its key has, retrieved by calling
     * {@link #getKey()}
     *
     * @return The value that its key contains
     */
    public Object getValue() {
        return value;
    }

    /**
     * This method returns a map containing the key-value pair passed into the
     * constructor.
     *
     * @return The key-value pair in a map
     */
    public Map<?, ?> getAsMap() {
        return map;
    }

    @Override
    public String toString() {
        return yaml;
    }

}
