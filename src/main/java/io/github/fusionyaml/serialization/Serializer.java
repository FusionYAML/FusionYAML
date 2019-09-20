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

import io.github.fusionyaml.events.SerializationListener;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.object.YamlList;
import io.github.fusionyaml.object.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer serializes a java object into a {@link YamlElement}. The {@link Object} can be
 * written into a YAML string. To deserialize a {@link YamlElement}, use a {@link Deserializer}.
 * <p>
 * Depending on some serializers, serializing a {@link java.util.Collection} returns a {@link YamlList},
 * serializing a {@link java.util.Map} returns a {@link YamlObject}, and serializing a primitive data or
 * a {@link String} returns {@link YamlPrimitive}.
 * <p>
 * Serializing an {@link Object} that is not mentioned above using the default serializer returns a
 * {@link YamlObject}. Each key in the {@link YamlObject} corresponds to a non-static field in the
 * serialized {@link Object}.
 * <p>
 * The keys' names in the serialized {@link Object} above will be the same as the non-static fields' names. The values will
 * also be equal to the values in the non-static fields in the {@link Object}. Therefore, the {@link YamlObject}'s size will
 * be equal to the number of the fields in the serialized {@link Object}.
 *
 * @param <T> The type of the object that will be serialized when calling {@link #serialize(Object)}
 */
public interface Serializer<T> {

    /**
    * Depending on some serializers, serializing a {@link java.util.Collection} returns a {@link YamlList},
    * serializing a {@link java.util.Map} returns a {@link YamlObject}, and serializing a primitive data or
    * a {@link String} returns {@link YamlPrimitive}.
    * <p>
    * Serializing an {@link Object} that is not mentioned above using the default serializer returns a
    * {@link YamlObject}. Each key in the {@link YamlObject} corresponds to a non-static field in the
    * serialized {@link Object}.
    * <p>
    * The keys' names in the serialized {@link Object} above will be the same as the non-static fields' names. The values will
    * also be equal to the values in the non-static fields in the {@link Object}. Therefore, the {@link YamlObject}'s size will
    * be equal to the number of the fields in the serialized {@link Object}.
     *
     * @param obj The {@link Object} that will be serialized
    */
    YamlElement serialize(@NotNull T obj);

    /**
     * Sets the {@link SerializationListener} for {@code this} object. When an {@link Object} is serialized,
     * {@link SerializationListener#onSerialization(Serializer, Object, YamlElement)} will be called in built-in serializers.
     *
     * @param listener The {@link SerializationListener}
     */
    void setOnSerialize(SerializationListener listener);

}
