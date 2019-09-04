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
package me.brokenearthdev.fusionyaml.configurations;

import me.brokenearthdev.fusionyaml.DefaultParser;
import me.brokenearthdev.fusionyaml.YamlException;
import me.brokenearthdev.fusionyaml.YamlParser;
import me.brokenearthdev.fusionyaml.object.YamlElement;
import me.brokenearthdev.fusionyaml.utils.StorageUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public ObjectConfiguration(Object object) {
        super();
        if (YamlUtils.isPrimitive(object) || object instanceof Collection || object instanceof Map)
            throw new YamlException("This configuration is not intended for Primitive objects, " +
                    String.class + ", " + Collection.class + ", and " + Map.class);
        super.object = serializer.serializeToElement(object).getAsYamlObject();
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
