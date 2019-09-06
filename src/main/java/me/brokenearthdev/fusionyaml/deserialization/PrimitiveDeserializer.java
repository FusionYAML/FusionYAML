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
import me.brokenearthdev.fusionyaml.serialization.ObjectSerializer;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.util.Map;

/**
 * Deserializes serialized primitive objects into deserialized ones
 */
public class PrimitiveDeserializer implements Deserializer {

    /**
     * The {@link DeserializationListener} for this object
     */
    private DeserializationListener listener;

    /**
     * Deserializes a serialized non-primitive, non-map, non-list object into a new
     * {@link Object}. In the built in deserializers, invoking this method other than
     * {@link ObjectDeserializer} will throw an {@link UnsupportedOperationException}
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
    @Override
    public <T> T deserializeObject(Map map, Class<T> clazz) {
        throw new UnsupportedOperationException("Not " + ObjectSerializer.class.getName() +": " + this.getClass().getName());
    }

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
    @Override
    public <T> T deserializeObject(YamlObject object, Class<T> clazz) throws YamlDeserializationException {
        throw new UnsupportedOperationException("Not " + ObjectSerializer.class.getName() +": " + this.getClass().getName());
    }

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
    @Override
    public Object deserialize(Object serializedObj) throws YamlDeserializationException {
        if (!YamlUtils.isPrimitive(serializedObj))
            throw new YamlDeserializationException("The object passed in is not a primitive nor a " + String.class.getName());
        if (listener != null)
            listener.onDeserialization(this, serializedObj, serializedObj);
        return serializedObj;
    }

    /**
     * Sets the {@link DeserializationListener} for {@code this} {@link Object}. When an {@link Object}
     * is deserialized, {@link DeserializationListener} is called.
     *
     * @param listener The {@link DeserializationListener}
     */
    @Override
    public void setOnDeserialization(DeserializationListener listener) {
        this.listener = listener;
    }
}
