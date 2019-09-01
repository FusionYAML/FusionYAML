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


import java.util.Collection;
import java.util.LinkedList;

/**
 * This class's role is to serialize {@link Collection}s of {@link Object}s
 */
public class CollectionSerializer extends ObjectSerializer {

    /**
     * This method serializes a {@link Collection} of {@link Object}s. Since {@link Collection}s
     * can be written directly into YAML, the method serializes {@link Object}s in the {@link Collection}.
     *
     * @param o The {@link Collection} of {@link Object}s to serialized
     * @return The serialized {@link Collection}
     * @throws YamlSerializationException Thrown if any reflective error(s) occurred
     * @throws IllegalArgumentException Thrown if the value passed in is not a {@link Collection}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> serialize(Object o) throws YamlSerializationException {
        if (!(o instanceof Collection))
            throw new IllegalArgumentException("The value passed in is not an instance of " + Collection.class.getName());
        Collection collection = (Collection) o;
        Collection serialized = new LinkedList();
        for (Object e : collection) {
            serialized.add(Serializers.OBJECT_SERIALIZER.serialize(e));
        }
        return serialized;
    }

}
