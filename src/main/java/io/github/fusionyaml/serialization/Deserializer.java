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
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import org.jetbrains.annotations.NotNull;

/**
 * Deserializes a {@link YamlElement} into an {@link Object} of type {@link T}. To serialize an {@link Object},
 * use a {@link Serializer}, which serializes the {@link Object} into a {@link YamlElement}.
 * <p>
 * Depending on some deserializers, deserializing a {@link io.github.fusionyaml.object.YamlList} returns a
 * {@link java.util.List}, deserializing a {@link io.github.fusionyaml.object.YamlPrimitive} returns a primitive
 * {@link Object} or a {@link String}, deserializing a {@link io.github.fusionyaml.object.YamlObject} in a
 * {@link MapTypeAdapter} returns a {@link java.util.Map}.
 * <p>
 * In {@link ObjectTypeAdapter}, deserializing a {@link io.github.fusionyaml.object.YamlObject} using
 * {@link ObjectTypeAdapter#deserializeObject(YamlObject, Class)} will return a {@code new} {@link Object}
 * instance of whatever the type is specified.
 * <p>
 * The deserialized {@link Object}'s instance will not have any of its constructor called, not even the default
 * constructor. The method may thrown a {@link io.github.fusionyaml.exceptions.YamlDeserializationException} if
 * the key-value pairs did not match all the fields' name and its values.
 *
 * @param <T> The object type that will be deserialized
 */
public interface Deserializer<T> {

    /**
     * Deserializes a {@link YamlElement} into an {@link Object} of type {@link T}.
     *
     * @param serialized The serialized {@link YamlElement}
     * @return The deserialized {@link YamlElement}
     * @throws io.github.fusionyaml.exceptions.YamlDeserializationException Thrown by some deserializers
     */
    T deserialize(@NotNull YamlElement serialized);

    /**
     * Sets a {@link DeserializationListener}, which is called by some deserializers when a {@link YamlElement}
     * is deserialized.
     *
     * @param listener The {@link DeserializationListener}
     */
    void setOnDeserialize(DeserializationListener listener);

}
