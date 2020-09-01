package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;

import java.lang.reflect.Type;

/**
 * A {@link TypeAdapter} is a bridge that is mainly used for serialization and
 * deserialization (conversion of objects to {@link YamlElement} and vice versa).
 * A {@link CollectionTypeAdapter}, for example, is responsible for serialization
 * and deserialization {@link java.util.Collection}s.
 * <p>
 * A {@link TypeAdapter} is used by {@link FusionYAML} for serialization and deserialization
 * as well, so anyone can have almost complete control on the de/serialization of objects.
 * To add a {@link TypeAdapter} to a {@link FusionYAML} object, consider using
 * {@link FusionYAML.Builder#addTypeAdapter(TypeAdapter, Type)}
 * <p>
 * To create a type adapter of your own, extend your class and specify the type in
 * the generics. So, for example, if you are creating a FooTypeAdapter, you should
 * extend {@code TypeAdapter<Foo>}
 *
 * @param <T> The type
 */
public abstract class TypeAdapter<T> {

    /**
     * This method serializes an object of the type passed in to a {@link YamlElement}
     *
     * @param obj  The object
     * @param type The type of the object passed in
     * @return A {@link YamlElement}, serialized from the object
     */
    public abstract YamlElement serialize(T obj, Type type);

    /**
     * Deserializes a {@link YamlElement}
     *
     * @param element The element
     * @param type    The type of object to deserialize into
     * @return An object of type {@link T}, deserialized from
     * {@link YamlElement}
     */
    public abstract T deserialize(YamlElement element, Type type);

}
