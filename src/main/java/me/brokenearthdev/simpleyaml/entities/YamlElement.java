package me.brokenearthdev.simpleyaml.entities;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

   
    private String asString;
    private Map<Object, Object> map;

    public YamlElement(@NotNull String key, @NotNull Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        Yaml yaml = new Yaml();
        this.key = key;
        this.value = value;
        this.map = new HashMap<>();
        this.map.put(key, value);
        this.asString = yaml.dump(map);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Map<?, ?> getAsMap() {
        return map;
    }

    public String getAsString() {
        return asString;
    }

    @Override
    public String toString() {
        return asString;
    }

}
