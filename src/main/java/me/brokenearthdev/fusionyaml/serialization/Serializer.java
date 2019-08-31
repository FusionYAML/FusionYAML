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

import java.util.*;

/**
 * This class contains methods that aid in object serialization. Depending on the {@link Object}
 * type, the serialized {@link Object} will be of different type except primitives and {@link String}s.
 * <p>
 * For example, in {@link ObjectSerializer}, the method {@link #serialize(Object)} will return a
 * {@link Map} of {@link String}s and {@link Object}s. Every index in the {@link Map} contains a
 * serialized {@link Object}.
 */
public abstract class Serializer {

    /**
     * This method serializes {@link Object}s into {@link Object}s that can be written into YAML {@link String}s,
     * {@link java.io.File}s, etc.
     * <p>
     * For example, If you serialized an {@link Object} of type {@code X}, where X is not a primitive, {@link List},
     * {@link Map}, or a {@link String}, the method will return a {@link Map}. Each entry in the {@link Map} is also
     * serialized.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param o The {@link Object} to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     */
    public abstract Object serialize(Object o) throws IllegalAccessException;

    /**
     * This method serializes {@link Object}s into {@link Object}s that can be written into YAML {@link String}s,
     * {@link java.io.File}s, etc. The method returns a {@link Map}, which contains the key-value pair whose value
     * is the serialized {@link Object}. The key's name is specified in the method's parameter
     * <p>
     * For example, If you serialized an {@link Object} of type {@code X}, where X is not a primitive, {@link List},
     * {@link Map}, or a {@link String}, the method will return a {@link Map}. Each entry in the {@link Map} is also
     * serialized.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param varName The key's name
     * @param o The {@link Object} to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     */
    public Map<String, Object> serialize(String varName, Object o) throws IllegalAccessException {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(varName, serialize(o));
        return linkedHashMap;
    }

    /**
     * This method serializes a {@link List} of {@link Object}s into a {@link List} that can be written
     * into YAML {@link String}s, {@link java.io.File}s, etc.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param objects The {@link Object}s to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     * @see #serialize(Object)
     */
    public List<Object> serializeAll(List<Object> objects) throws IllegalAccessException {
        return serializeAll((Collection<Object>) objects);
    }

    /**
     * This method serializes a {@link Collection} of {@link Object}s into a {@link List} that can be written
     * into YAML {@link String}s, {@link java.io.File}s, etc.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param objects The {@link Object}s to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     * @see #serialize(Object)
     */
    public List<Object> serializeAll(Collection<Object> objects) throws IllegalAccessException {
        List<Object> serialized = new LinkedList<>();
        for (Object object : objects) {
            serialize(object);
        }
        return serialized;
    }

    /**
     * This method serializes an array of {@link Object}s into a {@link List} that can be written
     * into YAML {@link String}s, {@link java.io.File}s, etc.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param objects The {@link Object}s to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     * @see #serialize(Object)
     */
    public List<Object> serializeAll(Object[] objects) throws IllegalAccessException {
        LinkedList<Object> serialized = new LinkedList<>();
        for (Object o : objects)
            serialized.add(serialize(o));
        return serialized;
    }

    /**
     * This method serializes a {@link List} of {@link Object}s into a {@link Map} that can be written into YAML
     * {@link String}s, {@link java.io.File}s, etc. The method returns a {@link Map}, which contains key-value pairs
     * whose value is the serialized {@link Object}. The key names for the {@link Map} that contains serialized
     * {@link Object} is specified at the parameter.
     * <p>
     * Every index in the {@link LinkedList}s will be the key-value pairs in the {@link Map} that contains
     * serialized {@link Object}s
     * <p>
     * For example, If you serialized an {@link Object} of type {@code X}, where X is not a primitive, {@link List},
     * {@link Map}, or a {@link String}, the method will return a {@link Map}. Each entry in the {@link Map} is also
     * serialized.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param varNames The key name
     * @param objects The {@link Object}s to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     */
    public Map<String, Object> serializeAll(LinkedList<String> varNames, LinkedList<Object> objects) throws IllegalAccessException {
        if (varNames.size() != objects.size())
            throw new IllegalArgumentException("The lists' sizes passed into the parameter is not equal");
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        LinkedList<Object> serialized = (LinkedList<Object>) serializeAll(objects);
        for (int i = 0; i < varNames.size(); i++)
            map.put(varNames.get(i), serialized.get(i));
        return map;
    }

    /**
     * This method serializes an array of {@link Object}s into a {@link Map} that can be written into YAML
     * {@link String}s, {@link java.io.File}s, etc. The method returns a {@link Map}, which contains key-value pairs
     * whose value is the serialized {@link Object}. The key names for the {@link Map} that contains serialized
     * {@link Object} is specified at the parameter.
     * <p>
     * Every index in the arrays will be the key-value pairs in the {@link Map} that contains serialized
     * {@link Object}s
     * <p>
     * For example, If you serialized an {@link Object} of type {@code X}, where X is not a primitive, {@link List},
     * {@link Map}, or a {@link String}, the method will return a {@link Map}. Each entry in the {@link Map} is also
     * serialized.
     * <p>
     * Serialized {@link Object}s can be the second parameter of
     * {@link me.brokenearthdev.fusionyaml.configurations.Configuration#set(String, Object)}.
     *
     * @param varNames The key name
     * @param objects The {@link Object}s to serialize
     * @return The serialized {@link Object}
     * @throws IllegalAccessException If any reflective errors occurred
     */
    public Map<String, Object> serializeAll(String[] varNames, Object[] objects) throws IllegalAccessException {
        if (varNames.length != objects.length)
            throw new IllegalArgumentException("The arrays' lengths aren't equal");
        LinkedList<String> vars = new LinkedList<>();
        LinkedList<Object> obj = new LinkedList<>();
        for (int i = 0; i < objects.length; i++) {
            vars.add(varNames[i]);
            obj.add(objects[i]);
        }
        return serializeAll(vars, obj);
    }

}
