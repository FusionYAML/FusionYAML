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

import io.github.fusionyaml.utils.YamlUtils;

/**
 * The class's role is to serialize a primitive or a {@link String}
 */
public class PrimitiveSerializer extends ObjectSerializer {

    /**
     * Since data of primitive types can be directly written to YAML {@link String}s without
     * requiring serialization, the method returns the {@link Object} initially passed in.
     *
     * @param o The {@link Object}
     * @return The {@link Object} initially passed in
     * @throws IllegalArgumentException If the {@link Object} passed in is not a primitive nor
     * a {@link String}
     */
    @Override
    public Object serialize(Object o) {
        if (!YamlUtils.isPrimitive(o))
            throw new IllegalArgumentException("The value passed in is not a primitive");
        if (listener != null)
            listener.onSerialization(this, o, o);
        return o;
    }

}
