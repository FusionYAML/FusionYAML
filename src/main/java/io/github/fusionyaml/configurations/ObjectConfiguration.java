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
package io.github.fusionyaml.configurations;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.parser.DefaultParser;
import io.github.fusionyaml.parser.YamlParser;
import io.github.fusionyaml.utils.StorageUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Serializes an {@link Object} and converts it to {@link YamlObject}. Methods
 * that removes path(s) that lead to a certain {@link Object} won't have any effects when called because by removing
 * them, it won't be possible to deserialize it back. The same applies to method that set data. However,
 * you can only change values and not remove them.
 * <p>
 * To deserialize the {@link Object}, call {@link #toObject(Class)}.
 */
public class ObjectConfiguration extends YamlConfiguration {

    /**
     * This constructor requires an {@link Object} that is:
     * <ul>
     *     <li>Not primitive</li>
     *     <li>Not a {@link String}</li>
     *     <li>Not a {@link Collection}</li>
     *     <li>Not a {@link Map}</li>
     * </ul>
     * <p>
     * The constructor will serialize the {@link Object} passed in and convert
     * the serialized {@link Object} into {@link YamlElement}.
     *
     * @param object The {@link Object} that will be deserialized
     */
    public ObjectConfiguration(Object object, FusionYAML yaml) {
        super(yaml);
        if (YamlUtils.isPrimitive(object) || object instanceof Collection || object instanceof Map)
            throw new YamlException("This configuration is not intended for Primitive objects, " +
                    String.class + ", " + Collection.class + ", and " + Map.class);
        super.object = objectTypeAdapter.serialize(object).getAsYamlObject();
    }

    public ObjectConfiguration(Object o) {
        this(o, new FusionYAML());
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     * <p>
     * If an {@link Object} other than the following is passed, the value will be set by converting it
     * to a {@link String} using the {@link #toString()} method present in the {@link Object}'s instance:
     * <ul>
     *     <li>primitive data types</li>
     *     <li>string</li>
     *     <li>maps</li>
     *     <li>lists</li>
     *     <li>YamlElements and its children</li>
     * </ul>
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull String path, Object value) {
        if (getContents().getMap().containsKey(path))
            super.set(path, value);
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     * <p>
     * The separator is a character that when used, the value will be set under the parent path, which
     * is the part of the path used before the separator. Calling this method while not using a separator
     * in the path is equivalent to calling {@link #set(String, Object)}.
     * <p>
     * If an {@link Object} other than the following is passed, the value will be set by converting it
     * to a {@link String} using the {@link #toString()} method present in the {@link Object}'s instance:
     * <ul>
     *     <li>primitive data types</li>
     *     <li>string</li>
     *     <li>maps</li>
     *     <li>lists</li>
     *     <li>YamlElements and its children</li>
     * </ul>
     *
     * @param path      The path to the value
     * @param separator The path separator. When used, the value will be set under the parent, which is
     *                  the section before the separator.
     * @param value     The value the path contains
     */
    @Override
    public void set(@NotNull String path, char separator, Object value) {
        set(StorageUtils.toList(path, separator), value);
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     * <p>
     * The method requires a {@link List} to be passed in. In an ascending order, every index in the
     * {@link List} is the child of the previous parent except the first index, which is the uppermost
     * parent.
     * <p>
     * If an {@link Object} other than the following is passed, the value will be set by converting it
     * to a {@link String} using the {@link #toString()} method present in the {@link Object}'s instance:
     * <ul>
     *     <li>primitive data types</li>
     *     <li>string</li>
     *     <li>maps</li>
     *     <li>lists</li>
     *     <li>YamlElements and its children</li>
     * </ul>
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull List<String> path, Object value) {
        if (value == null)
            return;
        YamlParser parser = new DefaultParser(YamlUtils.toMap0(getContents()));
        parser.map();
        if (parser.getObject(path) != null)
            super.set(path, value);
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull String path, YamlElement value) {
        set(Collections.singletonList(path), value);
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     * <p>
     * The separator is a character that when used, the value will be set under the parent path, which
     * is the part of the path used before the separator. Calling this method while not using a separator
     * in the path is equivalent to calling {@link #set(String, Object)}.
     *
     * @param path      The path to the value
     * @param separator The path separator. When used, the value will be set under the parent, which is
     *                  the section before the separator.
     * @param value     The value the path contains
     */
    @Override
    public void set(@NotNull String path, char separator, YamlElement value) {
        set(StorageUtils.toList(path, separator), value);
    }

    /**
     * Sets the value in the path. If the path doesn't exist, the method will do nothing.
     * <p>
     * The method requires a {@link List} to be passed in. In an ascending order, every index in the
     * {@link List} is the child of the previous parent except the first index, which is the uppermost
     * parent.
     *
     * @param path  The path to the value
     * @param value The value the path contains
     */
    @Override
    public void set(@NotNull List<String> path, YamlElement value) {
        Object o = YamlUtils.toElement(value, true);
        set(path, o);
    }

    /**
     * Invoking this method in this object won't do anything
     *
     * @param path The path to the key-value pair.
     */
    @Override
    public void removePath(@NotNull String path) {
    }

    /**
     * Invoking this method in this object won't do anything
     *
     * @param path      The path to the key-value pair.
     * @param separator The path separator. When used, the value will be set under the parent, which is
     */
    @Override
    public void removePath(@NotNull String path, char separator) {
    }

    /**
     * Invoking this method in this object won't do anything
     *
     * @param path The path to the key-value pair. Every index in the {@link List} is a descent.
     */
    @Override
    public void removePath(@NotNull List<String> path) {
    }

}