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
package me.brokenearthdev.fusionyaml.deserialization;

import me.brokenearthdev.fusionyaml.events.DeserializationListener;
import me.brokenearthdev.fusionyaml.exceptions.YamlDeserializationException;
import me.brokenearthdev.fusionyaml.object.YamlObject;

import java.util.Map;

/**
 * Contains methods that deserialize serialized {@link Object} into a regular class
 * {@link Object}. To deserialize an {@link Object} that is non-primitive, non-{@link java.util.List},
 * and non-{@link Map}, call {@link #deserializeObject(Map, Class)}. Otherwise, call
 * {@link #deserialize(Object)}
 */
public interface Deserializer {

    /**
     * Deserializes a serialized non-primitive, non-map, non-list object into a new
     * {@link Object}. In the built in deserializers, invoking this method in a class
     * other than {@link ObjectDeserializer} will throw an {@link UnsupportedOperationException}
     * <p>
     * {@link YamlDeserializationException} may be thrown if
     * <ul>
     *     <li>A reflective error had occurred, namely {@link IllegalAccessException}</li>
     *     <li>A deserialization error had occurred</li>
     * </ul>
     *
     * @param map The serialized {@link Map}, often retrieved by serializing a non-primitive, non-map,
     *            non-list class
     * @param clazz The specified class type. The deserializer will create and return an {@link Object}
     *              of this class type.
     * @param <T> The class type. The method will return an {@link Object} of this type.
     * @return The deserialized {@link Object}
     * @throws YamlDeserializationException Thrown when an {@link IllegalAccessException} is thrown or
     * when an error occurred while deserializing
     * @throws UnsupportedOperationException If {@code this} is not {@link ObjectDeserializer}
     */
    <T> T deserializeObject(Map map, Class<T> clazz) throws YamlDeserializationException;

    /**
     * Deserializes a {@link YamlObject} into a new {@link Object}. In the built in deserializers,
     * invoking this method in a class other than {@link ObjectDeserializer} will throw an
     * {@link UnsupportedOperationException}
     * <p>
     * {@link YamlDeserializationException} may be thrown if
     * <ul>
     *     <li>A reflective error had occurred, namely {@link IllegalAccessException}</li>
     *     <li>A deserialization error had occurred</li>
     * </ul>
     *
     * @param object The {@link YamlObject} that it yet be deserialized
     * @param clazz The specified class type. The deserializer will create and return an {@link Object}
     *              of this class type.
     * @param <T> The class type. The method will return an {@link Object} of this type.
     * @return The deserialized {@link Object}
     * @throws YamlDeserializationException Thrown when an {@link IllegalAccessException} is thrown or
     * when an error occurred while deserializing
     * @throws UnsupportedOperationException If {@code this} is not {@link ObjectDeserializer}
     */
    <T> T deserializeObject(YamlObject object, Class<T> clazz) throws YamlDeserializationException;

    /**
     * Deserializes an {@link Object} of primitive, {@link Map}, or {@link java.util.List}
     * type. If the {@link Object} passed into the parameter is not any of these types, the
     * method will return {@code null}.
     * <p>
     * To deserialize an {@link Object} not of these types, use {@link #deserializeObject(Map, Class)}
     *
     * @param serializedObj The serialized {@link Object}, often retrieved by
     * {@link me.brokenearthdev.fusionyaml.serialization.Serializer}
     * @return The deserialized {@link Object}
     * @throws YamlDeserializationException Thrown when an error occurred while deserializing
     */
    Object deserialize(Object serializedObj) throws YamlDeserializationException;

    /**
     * Sets the {@link DeserializationListener} for {@code this} {@link Object}. When an {@link Object}
     * is deserialized, {@link DeserializationListener} is called.
     *
     * @param listener The {@link DeserializationListener}
     */
    void setOnDeserialization(DeserializationListener listener);

}
