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
package io.github.fusionyaml.serialization;

import io.github.fusionyaml.events.DeserializationListener;
import io.github.fusionyaml.events.SerializationListener;
import io.github.fusionyaml.object.YamlElement;

/**
 * An implementation of {@link Serializer} and {@link Deserializer}. This abstract class overrides two
 * methods: {@link #setOnSerialize(SerializationListener)} and {@link #setOnDeserialize(DeserializationListener)}.
 * <p>
 * This abstract class also has two instance methods: {@link #callSerializationEvent(Object, YamlElement)}, and
 * {@link #callDeserializationEvent(Object, YamlElement)}. Both of these methods make a null check before calling
 * the appropriate event listener.
 * <p>
 * TypeAdapters are required if you want to create a custom serializer/deserializer, which are registered in
 * {@link io.github.fusionyaml.FusionYAMLBuilder}.
 *
 * @param <T> The object of type {@link T} that will be serialized / deserialized
 */
public abstract class TypeAdapter<T> implements Serializer<T>, Deserializer<T> {

    /**
     * The {@link DeserializationListener} for {@code this} object
     */
    protected DeserializationListener deserializationListener;

    /**
     * The {@link SerializationListener} for {@code this} object
     */
    protected SerializationListener serializationListener;

    @Override
    public void setOnDeserialize(DeserializationListener listener) {
        this.deserializationListener = listener;
    }

    @Override
    public void setOnSerialize(SerializationListener listener) {
        this.serializationListener = listener;
    }

    /**
     * Calls the {@link DeserializationListener} registered in this {@link TypeAdapter} object. If a
     * {@link DeserializationListener} is not registered, calling this method will do nothing.
     *
     * @param deserialized The object retrieved from deserializing a {@link YamlElement}
     * @param serialized The serialized object of type {@link T}
     */
    protected void callDeserializationEvent(T deserialized, YamlElement serialized) {
        if (deserializationListener != null)
            deserializationListener.onDeserialization(this, deserialized, serialized);
    }

    /**
     * Calls the {@link SerializationListener} registered in this {@link TypeAdapter} object. If a
     * {@link SerializationListener} is not registered, calling this method will do nothing.
     *
     * @param obj The object that was serialized
     * @param serialized The serialized object
     */
    protected void callSerializationEvent(T obj, YamlElement serialized) {
        if (serializationListener != null)
            serializationListener.onSerialization(this, obj, serialized);
    }

}