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

import me.brokenearthdev.fusionyaml.utils.ReflectionUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class contains method(s) that deserializes serialized objects into all kinds of
 * {@link Object}s. To deserialize a non-primitive, non-{@link Map}, non-{@link Collection},
 * and non-{@link String} object, use {@link #deserializeObject(Map, Class)}.
 */
public class ObjectDeserializer implements Deserializer {

    /**
     * The constant {@link Objenesis} instance
     */
    private static final Objenesis objenesis = new ObjenesisStd();

    /**
     * Deserializes a serialized non-primitive, non-map, non-list object into a new
     * {@link Object}. In the built in deserializers, invoking this method other than
     * {@link ObjectDeserializer} will throw an {@link UnsupportedOperationException}
     * <p>
     * {@link YamlDeserializationException} may be thrown if
     * <ul>
     *     <li>An {@link IllegalAccessException} was thrown</li>
     *     <li>One of the field(s) is
     *       <ul>
     *         <li>Non-primitive and non-string</li>
     *         <li>Non-string</li>
     *         <li>Not a {@link java.util.Collection}</li>
     *         <li>Not a {@link Map}</li>
     *       </ul>
     *     </li>
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
    public <T> T deserializeObject(Map map, Class<T> clazz) throws YamlDeserializationException {
        T t = objenesis.newInstance(clazz);
        List<Field> fields = ReflectionUtils.getNonStaticFields(t);
        boolean check = ReflectionUtils.checkNonPrimitive(fields);
        if (!check)
            throw new YamlDeserializationException("Invalid field type is found in the class. The fields should be " +
                    "primitive, a " + String.class.getName() + ", a " + Collection.class.getName() + ", or a " + Map.class.getName());
        boolean match = ReflectionUtils.isMatch(map, fields);
        if (!match)
            throw new YamlDeserializationException("The map passed in is not the deserialized object type");
        ReflectionUtils.assignFields(t, map, fields, Deserializers.OBJECT_DESERIALIZER);
        return t;
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
        if (serializedObj instanceof Collection)
            return Deserializers.COLLECTION_DESERIALIZER.deserialize(serializedObj);
        else if (serializedObj instanceof Map)
            return Deserializers.MAP_DESERIALIZER.deserialize(serializedObj);
        else if (YamlUtils.isPrimitive(serializedObj))
            return Deserializers.PRIMITIVE_DESERIALIZER.deserialize(serializedObj);
        else return null;
    }

}
