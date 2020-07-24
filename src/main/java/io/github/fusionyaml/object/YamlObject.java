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
package io.github.fusionyaml.object;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.serialization.TypeAdapter;
import io.github.fusionyaml.configurations.Configuration;
import io.github.fusionyaml.configurations.YamlConfiguration;
import io.github.fusionyaml.events.EntryChangeListener;
import io.github.fusionyaml.parser.YamlParser;
import io.github.fusionyaml.utils.StorageUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@code YamlObject} stores {@code YAML} key-value pairs in a {@link java.util.LinkedHashMap}
 * ready to be converted into {@code JSON} and a {@code YAML} {@link String}. You can also convert
 * this object is {@link JsonObject} via {@link #toJsonObject()}
 * <p>
 * A {@link YamlObject} can be thought of key-value pairs that can be retrieved, modified, and
 * stored.
 */
public class YamlObject implements YamlElement {

    /**
     * The {@link YamlParser.YamlType}
     */
    private YamlParser.YamlType type = YamlParser.YamlType.MAP;

    /**
     * The {@link EntryChangeListener} for this object
     */
    private EntryChangeListener listener;

    /**
     * The default {@link DumperOptions}
     */
    private static final DumperOptions defaultDumperOptions = defaultDumperOptions();

    /**
     * The constant {@link Gson} instance
     */
    private static final Gson GSON = new Gson();

    /**
     * A {@link LinkedHashMap} containing key-value pairs which essentially are where
     * all the {@code YAML} data are stored.
     */
    protected Map<String, YamlElement> map = new LinkedHashMap<>();

    /**
     * The {@link FusionYAML} instance
     */
    protected final FusionYAML fusionYAML;

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
        this(new FusionYAML());
    }

    /**
     * This constructor requires no objects to be passed into their parameters. An
     * empty {@link LinkedHashMap} is created upon initialization. The constructor sets
     * the {@link FusionYAML} field present this object to the value passed into the
     * constructor.
     */
    public YamlObject(@NotNull FusionYAML yaml) {
        this.fusionYAML = yaml;
        this.configuration = new YamlConfiguration(this);
    }

    /**
     * This constructor requires a {@link YamlParser.YamlType}
     * to be passed into the constructor. The {@link YamlParser.YamlType}
     * will be used when writing to a {@link java.io.File} through {@link Configuration}.
     * Upon initialization, an empty {@link FusionYAML} object will be created.
     *
     * @param type The {@link YamlParser.YamlType}
     */
    public YamlObject(@NotNull YamlParser.YamlType type) {
        this(type, new FusionYAML());
    }

    /**
     * This constructor requires a {@link YamlParser.YamlType}
     * to be passed into the constructor. The {@link YamlParser.YamlType}
     * will be used when writing to a {@link java.io.File} through {@link Configuration}.
     * The {@link FusionYAML} present this object will be set
     * equal to the value passed into this constructor.
     *
     * @param type The {@link YamlParser.YamlType}
     */
    public YamlObject(@NotNull YamlParser.YamlType type, @NotNull FusionYAML yaml) {
        this(yaml);
        this.type = type;
    }

    /**
     * This constructor requires a {@link Map} that contains {@code YAML} data expressed
     * in a map. By using this constructor, {@link #map} will be set equal to the value
     * passed into the constructor. An empty {@link FusionYAML} object will be created
     * upon initialization.
     * <p>
     * Calling {@code set} and {@code remove} methods will modify data in the map passed
     * in by adding and removing data, respectively.
     *
     * @param data The {@link Map} that contains {@code YAML} data
     */
    public YamlObject(@NotNull Map<String, YamlElement> data) {
        this(data, new FusionYAML());
    }

    /**
     * This constructor requires a {@link Map} that contains {@code YAML} data expressed
     * in a map. By using this constructor, {@link #map} will be set equal to the value
     * passed into the constructor. The {@link FusionYAML} present in this
     * object will be set equal to the value passed into the constructor.
     * <p>
     * Calling {@code set} and {@code remove} methods will modify data in the map passed
     * in by adding and removing data, respectively.
     *
     * @param data The {@link Map} that contains {@code YAML} data
     */
    public YamlObject(@NotNull Map<String, YamlElement> data, FusionYAML yaml) {
        this(yaml);
        map = data;
    }

    /**
     * This constructor requires a {@link Map} and a {@link YamlParser.YamlType}
     * The {@link Map} passed in is the YAML data. By using this constructor, {@link #map} will be set equal to
     * the value passed into the constructor. The {@link FusionYAML} will be initialized into an empty
     * {@link FusionYAML} object upon initialization.
     * <p>
     * Calling {@code set} and {@code remove} methods will modify data in the map passed
     * in by adding and removing data, respectively.
     *
     * @param data The {@link Map} that contains {@code YAML} data
     * @param type The {@link YamlParser.YamlType}
     */
    public YamlObject(@NotNull Map<String, YamlElement> data, @NotNull YamlParser.YamlType type) {
        this(new FusionYAML());
        this.map = data;
        this.type = type;
    }

    /**
     * This constructor requires a {@link Map} and a {@link YamlParser.YamlType}
     * The {@link Map} passed in is the YAML data. By using this constructor, {@link #map} will be set equal to
     * the value passed into the constructor. The {@link FusionYAML} present in this object will be set
     * equal to the value passed into the constructor.
     * <p>
     * Calling {@code set} and {@code remove} methods will modify data in the map passed
     * in by adding and removing data, respectively.
     *
     * @param data The {@link Map} that contains {@code YAML} data
     * @param type The {@link YamlParser.YamlType}
     */
    public YamlObject(@NotNull Map<String, YamlElement> data, @NotNull YamlParser.YamlType type, FusionYAML yaml) {
        this(yaml);
        this.map = data;
        this.type = type;
    }

    /**
     * A method that sets the value in a given key. A value is removed from a
     * key if the value passed in is {@code null}
     *
     * @param key The key that holds the value
     * @param value The value the key holds
     */
    private void change(@NotNull String key, YamlElement value) {
        if (value == null)
            map.remove(key);
        else {
            map.put(key, value);
        }
        if (listener != null)
            listener.onChange(this, Collections.singletonList(key), value);
    }

    /**
     * Deserializes this {@link YamlObject} into an object of {@link Type}
     * {@code T}
     *
     * @param type The type
     * @param <T>  The object type
     * @return An object of type {@code T}
     */
    public <T> T getAs(Type type) {
        TypeAdapter<T> adapter = fusionYAML.getTypeAdapter(type);
        return adapter.deserialize(this, type);
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
        if (value == null) {
            Map<String, Object> converted = YamlUtils.toMap0(this);
            Map<String, Object> map = YamlUtils.setNested(converted, paths, value);
            this.map = YamlUtils.toMap(map);
            return this;
        }
        if (listener != null)
            listener.onChange(this, paths, value);
        if (paths.size() == 1) {
            set(paths.get(0), value);
            return this;
        }
        // path is nested
        Map<String, Object> map = YamlUtils.toMap0(this);
        Map<String, Object> newMap = YamlUtils.setNested(map, paths, value);
        this.map = YamlUtils.toMap(YamlUtils.toStrMap(newMap));
        return this;
    }

    /**
     * Sets an {@link Object} in a specific path. The {@link Object} can be any class. Any {@link Object}s
     * passed in will be serialized then converted to {@link YamlElement}.
     *
     * @param path The path where the {@link Object} will be set to
     * @param value The {@link Object} that will be serialized
     * @return this object
     */
    public YamlObject set(@NotNull String path, Object value) {
        return set(Collections.singletonList(path), value);
    }

    /**
     * Sets an {@link Object} in a specific path. The {@link Object} can be any class. Any {@link Object}s
     * passed in will be serialized then converted to {@link YamlElement}.
     * <p>
     * The {@code separator} is a symbol used in the path given that when used, the method will set the
     * {@link Object} under this path (goes deeper). Not using the path separator in the {@code path} is
     * equivalent to calling {@link #set(String, Object)}
     *
     * @param path The path where the {@link Object} will be set to
     * @param separator The path separator
     * @param value The {@link Object} that will be serialized
     * @return this object
     */
    public YamlObject set(@NotNull String path, char separator, Object value) {
        return set(StorageUtils.toList(path, separator), value);
    }

    /**
     * Sets an {@link Object} in a specific path. The {@link Object} can be any class. Any {@link Object}s
     * passed in will be serialized then converted to {@link YamlElement}.
     * <p>
     * Every index in the {@link List} after at index {@code 0} is a descent. Each {@link String} found in
     * the index is the child of the {@link String} found at the previous index. The {@link String} found
     * at index {@code 0} is the uppermost path.
     *
     * @param path The path where the {@link Object} will be set to
     * @param value The {@link Object} that will be serialized
     * @return this object
     */
    public YamlObject set(@NotNull List<String> path, Object value) {
        if (path.size() == 0)
            return this;
        Object serialized = fusionYAML.serialize(value, value.getClass());
        YamlElement element = YamlUtils.toElement(serialized);
        return set(path, element);
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
        set(paths, null);
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
     * @return The {@link Map} that contains the key-value pairs that can be added or removed.
     */
    public Map<String, YamlElement> getMap() {
        return map;
    }

    /**
     * @return The {@link YamlParser.YamlType}
     */
    public YamlParser.YamlType getYamlType() {
        return type;
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
    @Nullable
    private YamlElement createElementPrimitive(Object o) {
        if (o == null)
            return null;
        return new YamlPrimitive(o);
    }

    /**
     * Gets the parsed {@link String} in YAML syntax from the {@link Map} that stores
     * key-value pairs, which are retrievable by {@link #getMap()}
     * <p>
     * Calling this method is equivalent to calling {@link #toString()}] as this method
     * returns a parsed {@link String} in YAML syntax.
     *
     * @return The parsed {@link String} in YAML syntax.
     */
    @NotNull
    public String toYamlString() {
        return toString();
    }

    @NotNull
    @Override
    public String toString() {
        Yaml yaml = new Yaml(fusionYAML.getDumperOptions());
        if (type == YamlParser.YamlType.LIST)
            return yaml.dump(StorageUtils.toList(YamlUtils.toMap0(this)));
        else return yaml.dump(YamlUtils.toMap0(this));
    }

    /**
     * @return The key-value pairs written in {@code JSON} syntax
     */
    public String toJsonString() {
        return new Gson().toJson(YamlUtils.toMap0(this));
    }

    /**
     * Loads a {@link JsonObject} from this object. The key-value pairs are
     * copied into the new {@link JsonObject} syntax.
     *
     * @return The loaded {@link JsonObject}
     */
    @NotNull
    public JsonObject toJsonObject() {
        return GSON.fromJson(toJsonString(), JsonObject.class);
    }

    /**
     * @return The default {@link DumperOptions}
     */
    @NotNull
    private static DumperOptions defaultDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
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
