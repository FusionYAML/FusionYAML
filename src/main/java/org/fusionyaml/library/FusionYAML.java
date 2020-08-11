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
package org.fusionyaml.library;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.fusionyaml.library.exceptions.YamlDeserializationException;
import org.fusionyaml.library.exceptions.YamlParseFailedException;
import org.fusionyaml.library.io.DocumentReader;
import org.fusionyaml.library.io.DocumentWriter;
import org.fusionyaml.library.io.MultiDocumentReader;
import org.fusionyaml.library.io.MultiDocumentWriter;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlNull;
import org.fusionyaml.library.object.YamlObject;
import org.fusionyaml.library.object.YamlPrimitive;
import org.fusionyaml.library.serialization.*;
import org.fusionyaml.library.utils.Utilities;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


/**
 * This is the main class in the project. This class can be viewed as the front-end
 * of the code in the FusionYAML project. You can load documents from invoking the
 * {@code #fromYAML} or {@code #fromJSON} methods.
 * <p>
 * You can dump yaml by calling the {@code #toYAML} and {@code #toJSON} methods. This
 * would allow you to convert {@link YamlElement}s in java to yaml (or json) text.
 * <p>
 * This class also stores different type adapters, which makes the serialization and
 * deserialization of objects much easier. There are different essential built-in
 * type adapters.
 * <p>
 * To create an empty FusionYAML instances you can call {@link #FusionYAML()}. Otherwise
 * you can utilize the {@link Builder} present in this class, allowing you to customize
 * a {@link FusionYAML} object.
 */
@SuppressWarnings("UnstableApiUsage")
public class FusionYAML {

    // Constant static fields
    private static final Gson GSON_DEFAULT = new GsonBuilder().create();
    private static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, Object>>() {
    }.getType();
    private static final String DOCUMENT_BEGIN = "---\n";
    private static final String DOCUMENT_END = "...\n";
    private static final YamlOptions YAML_DEFAULT_OPTIONS = new YamlOptions();

    // Constant type adapters
    public static final ObjectTypeAdapter<Object> OBJECT_TYPE_ADAPTER = new ObjectTypeAdapter();
    public static final CollectionTypeAdapter COLLECTION_TYPE_ADAPTER = new CollectionTypeAdapter();
    public static final MapTypeAdapter MAP_TYPE_ADAPTER = new MapTypeAdapter();
    public static final PrimitiveTypeAdapter PRIMITIVE_TYPE_ADAPTER = new PrimitiveTypeAdapter();
    public static final DateTypeAdapter DATE_TYPE_ADAPTER = new DateTypeAdapter();


    // Constant object fields
    private final Map<Type, TypeAdapter> classTypeAdapterMap;
    private final YamlOptions options;
    private final Yaml yaml;


    FusionYAML(YamlOptions options, Map<Type, TypeAdapter> adapterMap) {
        classTypeAdapterMap = adapterMap;
        this.options = options != null ? options : YAML_DEFAULT_OPTIONS;
        this.yaml = new Yaml(options == null ? YAML_DEFAULT_OPTIONS.dumperOptions() : options.dumperOptions());
        classTypeAdapterMap.put(Collection.class, new CollectionTypeAdapter<>(this));
        classTypeAdapterMap.put(Map.class, new MapTypeAdapter<>(this));
        classTypeAdapterMap.put(Number.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Boolean.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(String.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Character.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Enum.class, new EnumTypeAdapter<>(this));
        classTypeAdapterMap.put(new TypeToken<Object[]>(){}.getType(), new ArrayTypeAdapter<>(this));
        classTypeAdapterMap.put(Object.class, new ObjectTypeAdapter<>(this));
        classTypeAdapterMap.put(Date.class, new DateTypeAdapter(this));
    }


    /**
     * Initializes this class by setting default {@link TypeAdapter}s to important class types.
     * You can't modify {@link TypeAdapter}s in this {@link FusionYAML} object when you used this
     * constructor.
     * <p>
     * The constructor also sets the {@link DumperOptions} to default dumper options, which is
     * block flow style.
     */
    public FusionYAML() {
        this(YAML_DEFAULT_OPTIONS, new HashMap<>());
    }

    /**
     * @return The YAML options
     */
    public YamlOptions getYamlOptions() {
        return options;
    }

    /**
     * Creates a {@link DumperOptions} with its default flow style set to block flow style.
     *
     * @return The default dumper options
     */
    private static DumperOptions defaultDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    private static <T> TypeAdapter<T> getTypeAdapter(Map<Type, TypeAdapter> classTypeAdapterMap, Type as) {
        TypeAdapter adapter = null;
        int lpsCount = -1;
        Type adj = adjPrimitive(as);
        for (Map.Entry entry : classTypeAdapterMap.entrySet()) {
            int lps = lps((adj instanceof Class) ? (Class) adj :
                    TypeToken.of(adj).getRawType(), (Class) entry.getKey(), 0);
            if (lps != -1) {
                if (lpsCount > lps || lpsCount == -1) {
                    lpsCount = lps;
                    adapter = (TypeAdapter<?>) entry.getValue();
                }
            }
        }

        return adapter;
    }

    private static int lps(Class c1, Class c2, int lps) {
        if ((c1.isArray() && !c2.isArray()) || c2.isArray() && !c1.isArray()) return -1;
        if (c1.isArray()) {
            return lps(c1.getComponentType(), c2.getComponentType(), lps);
        }
        if (c1.equals(c2)) return lps;
        // prevent object.class from appearing as the
        // nearest superclass when a class is an implementation of an interface
        if ((c1.getInterfaces().length > 0) && c2.equals(Object.class)) return Integer.MAX_VALUE;
        if (c1.isInterface()) return lpsInt(c1, c2, lps);
        if (c1.getSuperclass() != null) {
            int contains = lps(c1.getSuperclass(), c2, lps + 1);
            int containsInt = lpsInt(c1, c2, lps + 1);
            return (containsInt == -1) ? contains : (contains == -1) ? containsInt : Math.min(contains, containsInt);
        } else return -1;
    }

    private static int lpsInt(Class c1, Class c2, int lps) {
        Class[] interfaces = c1.getInterfaces();
        for (Class clazz : interfaces) {
            int lpsInt = lpsInt(clazz, c2, lps + 1);
            if (clazz.equals(c2) || (lpsInt != -1))
                return (clazz.equals(c2)) ? lps + 1 : lpsInt;
        }
        return -1;
    }

    /**
     * Loads a raw YAML {@link String} into a {@link Map} of {@link String}s and {@link Object}s,
     * and then converts it to a {@link YamlElement}.
     * <p>
     * {@link YamlParseFailedException} may be thrown if an error
     * occurred while parsing. If the raw YAML {@link String} violates the YAML syntax,
     * {@link YamlParseFailedException} will be thrown.
     *
     * @param raw The raw YAML {@link String}
     * @return The {@link YamlElement} loaded from a raw YAML {@link String}
     * @throws YamlParseFailedException If an error had occurred while reading
     */
    public YamlElement fromYAML(String raw) throws YamlParseFailedException {
        try {
            try (DocumentReader reader = new DocumentReader(raw)) {
                return reader.readDocument();
            }
        } catch (Exception e) {
            throw new YamlParseFailedException(e);
        }
    }

    /**
     * Deserializes a {@link YamlElement} into a class of type {@link T}. The method calls the appropriate
     * type adapter for this object type. If on suitable type adapter is found, an {@link ObjectTypeAdapter}
     * will be used to deserialize the {@link YamlElement} into a class of type {@link T}.
     * <p>
     * If the {@link YamlElement} is a {@link YamlObject}, and there is no appropriate type adapter for class
     * type {@link T}, the {@link ObjectTypeAdapter} will create a new instance. No constructors will be called
     * upon initialization, so even default class constructor will have no effect.
     * <p>
     * Next, the {@link ObjectTypeAdapter} will treat every entry in the {@link YamlObject} as a field. The
     * each entry's path represents a field's name. The field's value, since it is a {@link YamlElement}, will
     * be deserialized into an appropriate {@link Object} to set its value to the field.
     * <p>
     * On the other hand, if there is no suitable {@link TypeAdapter} and the {@link YamlElement} is not a
     * {@link YamlObject}, {@link ObjectTypeAdapter#deserialize(YamlElement, Type)} will be called. The method will
     * 'lazy' deserialize the {@link Object}. The outcome may not be as expected.
     * <p>
     * For example, if you added a {@link TypeAdapter} in a different {@link FusionYAML} object and you serialized it
     * to a {@link YamlPrimitive} in this object, and this {@link Object} didn't have the
     * {@link TypeAdapter} registered, the {@link ObjectTypeAdapter} will deserialize it to a {@link String}
     *
     * @param element The {@link YamlElement}, which is a serialized form of an object of type
     *                {@link T}
     * @param as The class to deserialize into.
     * @param <T> The class type
     * @return The deserialized {@link YamlElement} of type {@link T}
     * @throws YamlDeserializationException Thrown when an error occurred while
     * deserializing
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(YamlElement element, Type as) {
        TypeAdapter<T> adapter = getTypeAdapter(classTypeAdapterMap, as);
        if (adapter == null) adapter = new ObjectTypeAdapter<>(this);
        return adapter.deserialize(element, as);
    }

    public YamlElement fromYAML(File file) {
        try {
            return this.fromYAML(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new YamlParseFailedException(e);
        }
    }

    /**
     * Reads YAML from the {@link Reader} provided and then loads it into an
     * {@link Object} to be then loaded to a {@link YamlElement} depending on its
     * type
     * <p>
     * {@link YamlParseFailedException} may be thrown if an error
     * occurred while parsing or if the parser can't construe elements in the string.
     * {@link IOException} may also be thrown if an IO error occurred.
     *
     * @param reader The reader from which the contents will be extracted
     * @return A {@link YamlObject} loaded from the reader
     * @throws YamlParseFailedException If an IO error had occurred while reading
     */
    public YamlElement fromYAML(Reader reader) throws YamlParseFailedException {
        try (DocumentReader docReader = new DocumentReader(reader)) {
            return docReader.readDocument();
        } catch (IOException e) {
            throw new YamlParseFailedException(e);
        }
    }

    /**
     * Loads the JSON {@link String} into a {@link YamlElement}. The raw JSON {@link String} will first be
     * converted to a {@link Map} of {@link String}s and {@link Object}s before loading it to a {@link YamlElement}
     *
     * @param raw The raw JSON {@link String}
     * @return The {@link YamlElement} loaded from a raw JSON {@link String}
     */
    public YamlElement fromJSON(String raw) {
        Map<String, Object> map = GSON_DEFAULT.fromJson(raw, MAP_TYPE);
        return Utilities.toYamlObject(map);
    }

    /**
     * Deserializes a raw YAML {@link String} into an object of type {@link T}. Please note that the {@link String}
     * should contain only one document or {@link YamlParseFailedException} will
     * be thrown. The method will look for appropriate {@link TypeAdapter}s for the {@link Class} of type {@link T}
     * and then use the {@link TypeAdapter} to deserialize.
     * <p>
     * If no such {@link TypeAdapter} is found, {@link ObjectTypeAdapter} will be used to deserialize the YAML
     * {@link String} into a {@link Class} of type {@link T}. The type adapter will create a new instance of
     * {@link T} without calling a constructor, even the default one.
     * <p>
     * If an error occurred during parsing, {@link YamlParseFailedException} will
     * be thrown. One of the most common causes of thus exception is invalid YAML syntax, which is detected during
     * mapping.
     * <p>
     * If an error occurred while deserializing, {@link YamlDeserializationException}
     * will be thrown.
     *
     * @param yaml The raw YAML {@link String}
     * @param as The {@link Class} of type {@link T} to deserialize into
     * @param <T> The type
     * @return The {@link String} YAML deserialized into an {@link Object} of {@link T}
     * @throws YamlParseFailedException Thrown when something wrong occurred during parsing
     * @throws YamlDeserializationException Thrown when an error occurred
     * during deserializing
     */
    public <T> T deserialize(String yaml, Class<T> as) {
        return deserialize(fromYAML(yaml), as);
    }

    /**
     * Converts a {@link YamlElement} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlElement} object to a JSON {@link String}.
     *
     * @param element The {@link YamlElement}
     * @param gson    The {@link Gson} instance
     * @return The JSON {@link String}, which is converted from the {@link YamlElement} object.
     */
    public String toJSON(YamlElement element, Gson gson) {
        return gson.toJson(Utilities.toObject(element));
    }

    /**
     * Converts a {@link YamlElement} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlElement} object to a JSON {@link String}.
     *
     * @param element The {@link YamlElement}
     * @param pretty  Whether the method should use {@link Gson}'s pretty printing or not
     * @return The JSON {@link String}, which is converted from the {@link YamlObject} object.
     */
    public String toJSON(YamlElement element, boolean pretty) {
        final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(element, gson);
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will first parse the YAML and convert it
     * to a {@link Map} of {@link String}s and {@link Object}s. Next, the method will use the {@link Gson}'s pretty
     * print-enabled object to convert it to a JSON {@link String} if the {@code boolean} passed in was true.
     *
     * @param yaml   The YAML {@link String}
     * @param pretty Whether the JSON {@link String} should be prettified or not
     * @return The JSON {@link String} converted from a YAML {@link String}
     */
    public String toJSON(String yaml, boolean pretty) {
        final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(yaml, gson);
    }

    /**
     * Converts a {@link YamlElement} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlElement} object to a JSON {@link String}.
     * The method will use default {@link Gson} to convert the {@link Map} of {@link String}s and {@link Object}s to
     * a JSON string.
     *
     * @param element The {@link YamlElement}
     * @return The JSON {@link String}, which is converted from the {@link YamlObject} object.
     */
    public String toJSON(YamlElement element) {
        return toJSON(element, false);
    }

    private static Type adjPrimitive(Type type) {
        if (type.getTypeName().equals("int")) return Integer.class;
        if (type.getTypeName().equals("double")) return Double.class;
        if (type.getTypeName().equals("float")) return Float.class;
        if (type.getTypeName().equals("char")) return Character.class;
        if (type.getTypeName().equals("byte")) return Byte.class;
        if (type.getTypeName().equals("long")) return Long.class;
        if (type.getTypeName().equals("short")) return Short.class;
        if (type.getTypeName().equals("boolean")) return Boolean.class;
        return type;
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will first parse the YAML and convert it
     * to an {@link Object}. Next, the method will use the {@link Gson} instance passed in to convert it to a
     * YAML {@link String}
     *
     * @param yaml The YAML {@link String}
     * @param gson The {@link Gson} object
     * @return The JSON {@link String} converted from a YAML {@link String}
     */
    public String toJSON(String yaml, Gson gson) {
        Object load = this.yaml.load(yaml);
        return gson.toJson(load);
    }

    /**
     * Converts a {@link YamlElement} to a {@link JsonElement}
     *
     * @param element The element
     * @return The {@link JsonElement} converted from a {@link YamlElement} object
     */
    public JsonElement toJSONElement(YamlElement element) {
        return GSON_DEFAULT.toJsonTree(Utilities.toDumpableObject(element)).getAsJsonObject();
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will use the default {@link Gson} object
     * to convert it from an {@link Object}, to a JSON {@link String}.
     * <p>
     * If an error occurred while parsing, {@link YamlParseFailedException}
     * will be thrown.
     *
     * @param yaml The YAML {@link String}
     * @return The JSON {@link String}, converted from a JSON {@link String}
     */
    public String toJSON(String yaml) {
        return toJSON(yaml, false);
    }

    public void toYAML(YamlElement element, Writer out) {
        try (DocumentWriter writer = new DocumentWriter(out)) {
            writer.write(element, this);
        } catch (IOException e) {
            throw new YamlParseFailedException(e);
        }
    }

    /**
     * Converts a {@link YamlElement} to a dumpable {@link Object} and then utilize
     * snakeyaml's dumper to dump the object
     *
     * @param element The element
     * @return A YAML string
     */
    public String toYAML(YamlElement element) {
        return yaml.dump(Utilities.toObject(element));
    }

    public Iterable<YamlElement> fromMultidocYAML(Reader reader) {
        try {
            try (MultiDocumentReader multiDocumentReader = new MultiDocumentReader(reader)) {
                return multiDocumentReader.readDocuments();
            }
        } catch (IOException e) {
            throw new YamlParseFailedException(e);
        }
    }

    public Iterable<YamlElement> fromMultidocYAML(File file) {
        try {
            return this.fromMultidocYAML(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new YamlParseFailedException(e);
        }
    }

    public Iterable<YamlElement> fromMultidocYAML(String string) {
        return fromMultidocYAML(new StringReader(string));
    }

    /**
     * Gets the appropriate {@link TypeAdapter} for the class. If no appropriate {@link TypeAdapter} is found, the
     * method will return an {@link ObjectTypeAdapter} of type V.
     *
     * @param clazz The class
     * @param <V> The type of the TypeAdapter
     * @return The appropriate {@link TypeAdapter} for the class. If no such {@link TypeAdapter} is found,
     * an {@link ObjectTypeAdapter} of type V will be returned
     */
    public <V> TypeAdapter<V> getTypeAdapter(Class<?> clazz) {
        return getTypeAdapter((Type) clazz);
    }

    /**
     * Gets the appropriate {@link TypeAdapter} for the class. If no appropriate {@link TypeAdapter} is found, the
     * method will return {@code null}
     *
     * @param type The type
     * @param <V> The type of the type adapter
     * @return The appropriate {@link TypeAdapter} for the class. If no such {@link TypeAdapter} is found,
     * {@code null} is returned
     */
    public <V> TypeAdapter<V> getTypeAdapter(Type type) {
        TypeAdapter<V> adapter = getTypeAdapter(classTypeAdapterMap, type);
        return adapter == null ? new ObjectTypeAdapter<>() : adapter;
    }

    /**
     * Serializes an {@link Object} into a {@link YamlElement}. The method wil look for the {@link TypeAdapter}
     * of the provided object's type and use it to serialize the object. If no appropriate {@link TypeAdapter}
     * is found, {@link ObjectTypeAdapter} will be used to serialize the {@link Object}.
     * <p>
     * The {@link ObjectTypeAdapter} will convert all local fields into key-value pairs. Each key-value pair
     * is a field's name and the field's value, respectively.
     *
     * @param o The {@link Object} to serialize
     * @param type The {@link Type} of the object passed in. In some cases, the value
     *             passed in is ignored depending on the type adapter invoked. However, it is
     *             recommended to pass in the type of the object with a {@link TypeToken} if
     *             applicable.
     * @return The {@link YamlElement}, which is serialized from a java {@link Object}
     */
    @SuppressWarnings("unchecked")
    public YamlElement serialize(Object o, Type type) {
        if (o == null) return YamlNull.NULL;
        TypeAdapter adapter = getTypeAdapter(type);
        return adapter.serialize(o, type);
    }


    private static boolean builtInAdapter(TypeAdapter<?> adapter) {
        return adapter instanceof ArrayTypeAdapter || adapter instanceof CollectionTypeAdapter ||
                adapter instanceof DateTypeAdapter || adapter instanceof EnumTypeAdapter ||
                adapter instanceof MapTypeAdapter || adapter instanceof ObjectTypeAdapter ||
                adapter instanceof PrimitiveTypeAdapter;
    }

    public void toMultidocYAML(Iterable<YamlElement> elements, Writer out) {
        try {
            try (MultiDocumentWriter writer = new MultiDocumentWriter(out)) {
                writer.writeDocuments(elements, this);
            }
        } catch (IOException e) {
            throw new YamlParseFailedException(e);
        }
    }

    public String toMultidocYAML(Iterable<YamlElement> elements) {
        StringWriter writer = new StringWriter();
        toMultidocYAML(elements, writer);
        return writer.toString();
    }

    /**
     * A builder for the {@link FusionYAML} class
     */
    public static class Builder {

        // type and type adapter map, used to store type adapters and pairing
        // then to a specific type
        private final Map<Type, TypeAdapter> ctaMap = new LinkedHashMap<>();

        // An instance of YamlOption's builder
        private final YamlOptions.Builder builder = new YamlOptions.Builder();

        /**
         * @param adapter The {@link TypeAdapter} that'll be paired with the type
         * @param type    The {@link Type} that'll be serialized or deserialized
         *                using the {@link TypeAdapter}
         * @return This instance
         */
        public Builder addTypeAdapter(TypeAdapter<?> adapter, Type type) {
            ctaMap.put(type, adapter);
            return this;
        }

        /**
         * Adds a type adapter if it isn't registered and isn't built-in.
         *
         * @param adapter The {@link TypeAdapter} that'll be paired with the type
         *                if it doesn't exist in one of the values
         * @param type    The {@link Type} that'll be serialized or deserialized
         *                using the {@link TypeAdapter}
         * @return This instance
         */
        public Builder addTypeAdapterIfNotExists(TypeAdapter<?> adapter, Type type) {
            if (!builtInAdapter(adapter) && !ctaMap.containsValue(adapter))
                ctaMap.put(type, adapter);
            return this;
        }

        public Builder onlyExposed(boolean onlyExposed) {
            builder.setOnlyExposed(onlyExposed);
            return this;
        }

        public Builder enumNameMentioned(boolean onlyEnumNameMentioned) {
            builder.setMentionEnumName(onlyEnumNameMentioned);
            return this;
        }

        public Builder excludeNullValues(boolean excl) {
            builder.setExcludeNullValues(excl);
            return this;
        }

        public Builder canonical(boolean canonical) {
            builder.setCanonical(canonical);
            return this;
        }

        public Builder allowUnicode(boolean allowUnicode) {
            builder.setAllowUnicode(allowUnicode);
            return this;
        }

        public Builder prettyFlow(boolean prettyFlow) {
            builder.setPrettyFlow(prettyFlow);
            return this;
        }

        public Builder splitLinesOverWidth(boolean splitLines) {
            builder.setSplitLines(splitLines);
            return this;
        }

        public Builder allowDocStartAndEnd(boolean docStartAndEnd) {
            builder.setAllowDocStartAndEnd(docStartAndEnd);
            return this;
        }

        public Builder indent(int indent) {
            builder.setIndent(indent);
            return this;
        }

        public Builder width(int width) {
            builder.setWidth(width);
            return this;
        }

        public Builder maxKeyLength(int maxKeyLength) {
            builder.setMaxKeyLength(maxKeyLength);
            return this;
        }

        public Builder timezone(TimeZone timeZone) {
            builder.setTimezone(timeZone);
            return this;
        }

        public Builder scalarStyle(DumperOptions.ScalarStyle scalarStyle) {
            builder.setScalarStyle(scalarStyle);
            return this;
        }

        public Builder version(DumperOptions.Version version) {
            builder.setVersion(version);
            return this;
        }

        public Builder linebreak(DumperOptions.LineBreak lineBreak) {
            builder.setLineBreak(lineBreak);
            return this;
        }

        public Builder nonPrintableStyle(DumperOptions.NonPrintableStyle nonPrintableStyle) {
            builder.setNonPrintableStyle(nonPrintableStyle);
            return this;
        }

        public Builder flowStyle(DumperOptions.FlowStyle flowStyle) {
            builder.setFlowStyle(flowStyle);
            return this;
        }

        public FusionYAML build() {
            return new FusionYAML(builder.build(), new LinkedHashMap<>());
        }

    }

}