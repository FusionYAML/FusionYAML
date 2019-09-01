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
package me.brokenearthdev.fusionyaml.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The class's role is to serialize {@link Object}s in {@link Map}s.
 */
public class MapSerializer extends ObjectSerializer {

    /**
     * Serializes all {@link Object}s in the {@link Map} passed in. Since {@link Map}s
     * can be written directly into YAML, the method serializes {@link Object}s in
     * the {@link Map}.
     *
     * @param o The {@link Map} to serialize
     * @return The serialized {@link Map}
     * @throws YamlSerializationException Thrown if any reflective error(s) occurred
     * @throws IllegalArgumentException Thrown if the value passed in is not a {@link Map}
     */
    @Override
    public Map<String, Object> serialize(Object o) throws YamlSerializationException {
        if (!(o instanceof Map))
            throw new IllegalArgumentException("The value passed in is not a map");
        Map<String, Object> serialized = new LinkedHashMap<>();
        Map map = (Map) o;
        for (Object key : map.keySet()) {
            serialized.put(key.toString(), Serializers.OBJECT_SERIALIZER.serialize(map.get(key)));
        }
        return serialized;
    }

}
