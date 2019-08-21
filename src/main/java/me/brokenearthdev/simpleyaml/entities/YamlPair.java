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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import me.brokenearthdev.simpleyaml.utils.StorageUtils;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.List;
import java.util.Map;

/**
 * YamlElement is an immutable class that houses key-value pair(s). A value in this class can be a map
 * that also houses key-value pair(s), which can also house key-value pair(s), and so on. This kind of data arrangement
 * is known as tree. A value in this class can also be a list that houses key-value pairs.
 * <p>
 * The key in this class is the highest in the hierarchy. There can be only one outermost key.
 */
public class YamlPair {

    private static DumperOptions defaultDumperOptions;

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
     * Key-value pairs stored in an immutable map or list
     */
    private Object object;

    /**
     * This constructor requires a key-value pair to be passed into its parameters where
     * the key is the highest in the hierarchy containing a value. The value may contain
     * nodes.
     * <p>
     * Using separators to represent a descent in hierarchy will not work. Instead, pass in
     * a map as the value.
     *
     * @param key The key that contains a value
     * @param value The value that its key contains
     * @param options A {@link DumperOptions} instance which contains convenient options t
     *                hat adjust styles in yaml to meet your preferences
     */
    public YamlPair(@NotNull String key, @NotNull Object value, DumperOptions options) {
        initDefaultOptions();
        Yaml yaml = (options != null) ? new Yaml(options) : new Yaml(defaultDumperOptions);
        this.key = key;
        this.value = value;
        this.object = new ImmutableMap.Builder<Object, Object>()
            .put(key, value)
            .build();
        this.yaml = yaml.dump(options);
    }

    /**
     * This constructor requires a key-value pair to be passed into its parameters where
     * the key is the highest in the hierarchy containing a value. The value may contain
     * nodes.
     * <p>
     * Using separators to represent a descent in hierarchy will not work. Instead, pass in
     * a map as the value.
     *
     * @param key The key that contains a value
     * @param value The value that its key contains
     */
    public YamlPair(@NotNull String key, @NotNull Object value) {
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
    public YamlPair(@NotNull Map<Object, Object> map, DumperOptions options) {
        if (map.size() != 1)
            throw new YAMLException("The size of the map passed in is not equal to one");
        initDefaultOptions();
        for (Map.Entry entry : map.entrySet()) {
            this.key = entry.getKey().toString();
            this.value = entry.getValue();
        }
        if (key == null || value == null)
            throw new YAMLException("One of the objects in the map is null");
        this.object = StorageUtils.toImmutableMap(map);
        Yaml yaml = (options != null) ? new Yaml(options) : new Yaml(defaultDumperOptions);
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
    public YamlPair(@NotNull Map<Object, Object> map) {
        this(map, null);
    }

    public YamlPair(@NotNull List<Object> list, DumperOptions options) {
        initDefaultOptions();
        this.object = list;
        Yaml yaml = (options == null) ? new Yaml(defaultDumperOptions) : new Yaml(options);
        this.yaml = yaml.dump(list);
        ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
        list.forEach(builder::add);
        this.object = builder.build();
    }

    public YamlPair(@NotNull List<Object> list) {
        this(list, null);
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
     * This method returns a map or a list containing the key-value pair passed into the
     * constructor.
     *
     * @return The key-value pair in a map or list
     */
    public Object getAsObject() {
        return object;
    }

    /**
     * This method converts yaml key-value pair passed into the constructor to
     * Json.
     *
     * @return Json converted from Yaml
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    @Override
    public String toString() {
        return yaml;
    }

    private static void initDefaultOptions() {
        if (defaultDumperOptions == null)
            defaultDumperOptions = new DumperOptions();
        defaultDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

}
