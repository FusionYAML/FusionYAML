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
package io.github.fusionyaml;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.parser.DefaultParser;
import io.github.fusionyaml.parser.YamlParser;
import io.github.fusionyaml.adapters.*;
import io.github.fusionyaml.utils.FileUtils;
import io.github.fusionyaml.utils.ReflectionUtils;
import io.github.fusionyaml.utils.YamlUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This is the main class used to manage YAML stuff. Load YAML with {@link #fromYaml(Map)} and
 * {@link #fromYaml(Map, YamlParser.YamlType)}, convert YAML to JSON with toJSON methods, serialize deserialize
 * {@link Object}s with deserialize and deserialize methods, and retrieve {@link TypeAdapter}s with
 * {@link #getTypeAdapter(Class)}
 * <p>
 * To construct a {@link FusionYAML} object with your {@link TypeAdapter}s incorporated into the object, use
 * a {@link FusionYAMLBuilder}. Instead of using a {@link YamlParser} to convert a YAML string into a {@link Map}
 * of {@link String}s and {@link Object}s, you can use this object and call {@link #fromYaml(String)}. Moreover, you
 * can simply parse raw YAML with {@link #parseYAML(String)} and {@link #parseYAML(String, YamlParser)}.
 * <p>
 * You can also load YAML from JSON using fromJSON methods. Upon invoking some toJSON methods, the method converts
 * the data passed into the method's parameter to a {@link Map} of {@link String}s and {@link Object}s, before using
 * {@link Gson} to convert YAML into JSON.
 * <p>
 * Please note that this class can only parse one document at a time, meaning that using multiple yaml documents
 * in a {@link String} and then loading will throw an exception.
 */
@SuppressWarnings("UnstableApiUsage")
public class FusionYAML {

    // Constant static fields
    private static final Gson GSON_DEFAULT = new GsonBuilder().create();
    private static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();
    private static final String DOCUMENT_BEGIN = "---\n";
    private static final String DOCUMENT_END = "...\n";
    private static final YamlParser DEFAULT_PARSER = new DefaultParser(DOCUMENT_BEGIN + DOCUMENT_END);
    private static final YamlParser.YamlType DEFAULT_YAML_TYPE = YamlParser.YamlType.MAP;
    private static final DumperOptions YAML_DEFAULT_DUMPER_OPTIONS = defaultDumperOptions();

    // Constant type adapters
    public static final ObjectTypeAdapter<Object> OBJECT_TYPE_ADAPTER = new ObjectTypeAdapter();
    public static final CollectionTypeAdapter COLLECTION_TYPE_ADAPTER = new CollectionTypeAdapter();
    public static final MapTypeAdapter MAP_TYPE_ADAPTER = new MapTypeAdapter();
    public static final PrimitiveTypeAdapter PRIMITIVE_TYPE_ADAPTER = new PrimitiveTypeAdapter();
    public static final DateTypeAdapter DATE_TYPE_ADAPTER = new DateTypeAdapter();


    // Constant object fields
    private final Map<Type, TypeAdapter> classTypeAdapterMap;
    private final DumperOptions dumperOptions;
    private final Yaml yaml;


    FusionYAML(DumperOptions options, Map<Type, TypeAdapter> adapterMap, List<Class> removeOptional) {
        classTypeAdapterMap = adapterMap;
        this.dumperOptions = options != null ? options : YAML_DEFAULT_DUMPER_OPTIONS;
        this.yaml = new Yaml(dumperOptions);
        classTypeAdapterMap.put(Collection.class, new CollectionTypeAdapter(this));
        classTypeAdapterMap.put(Map.class, new MapTypeAdapter(this));
        classTypeAdapterMap.put(Number.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Boolean.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(String.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Character.class, new PrimitiveTypeAdapter(this));
        classTypeAdapterMap.put(Object.class, new ObjectTypeAdapter<>(this));
        addOptionalIfNotRemoved(Date.class, new DateTypeAdapter(this), removeOptional);
    }

    private boolean addOptionalIfNotRemoved(Class<?> c, TypeAdapter adapter, List<Class> removeOptional) {
        if (!removeOptional.contains(c)) {
            classTypeAdapterMap.put(c, adapter);
            return true;
        }
        return false;
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
        this(YAML_DEFAULT_DUMPER_OPTIONS, new HashMap<>(), new ArrayList<>());
    }

    /**
     * @return The {@link DumperOptions} in this {@link FusionYAML} object
     */
    public DumperOptions getDumperOptions() {
        return dumperOptions;
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

    /**
     * Loads a raw YAML {@link String} into a {@link Map} of {@link String}s and {@link Object}s,
     * and then converts it to a {@link YamlObject}.
     * <p>
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} may be thrown if an error
     * occurred while parsing. If the raw YAML {@link String} violates the YAML syntax,
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} will be thrown.
     *
     * @param raw The raw YAML {@link String}
     * @return The {@link YamlObject} loaded from a raw YAML {@link String}
     */
    public YamlObject fromYaml(String raw) {
        Map<String, YamlElement> mem = YamlUtils.toMap(parseYAML(raw));
        return new YamlObject(mem, DEFAULT_PARSER.getYamlType());
    }

    /**
     * Reads YAML from the {@link Reader} provided and then loads it into a {@link Map} of {@link String}s
     * and {@link Object} before converting it into a {@link YamlObject}
     * <p>
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} may be thrown if an error
     * occurred while parsing or if the parser can't construe elements in the string.
     * {@link IOException} may also be thrown if an IO error occurred.
     *
     * @param reader The reader from which the contents will be extracted
     * @return A {@link YamlObject} loaded from the reader
     * @throws IOException If an IO error had occurred while reading
     */
    public YamlObject fromYaml(Reader reader) throws IOException {
        return fromYaml(FileUtils.readToString(reader));
    }

    /**
     * Parses YAML using a {@link YamlParser} into a {@link Map} of {@link String}s and {@link Object}s.
     * The parser passed in will first be reloaded using {@link YamlParser#reload(Map)} before mapping it
     * into a {@link Map} of {@link String}s and {@link Object}s.
     * <p>
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} may be thrown if an error occurred
     * while parsing. If the raw YAML {@link String} violates the YAML syntax,
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} will be thrown.
     *
     * @param raw The raw YAML {@link String}
     * @param parser The {@link YamlParser} that will be used to parse the raw YAML {@link String}
     *               passed into the method.
     * @return A {@link Map} of {@link String}s and {@link Object}s, parsed from a raw YAML {@link String}
     * using a {@link YamlParser}
     */
    public Map<String, Object> parseYAML(String raw, YamlParser parser) {
        parser.reload(raw);
        return parser.map();
    }

    /**
     * Parses YAML using a {@link YamlParser} into a {@link Map} of {@link String}s and {@link Object}s.
     * The parser passed in will first be reloaded using {@link YamlParser#reload(Map)} before mapping it
     * into a {@link Map} of {@link String}s and {@link Object}s.
     * <p>
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} may be thrown if an error occurred
     * while parsing. If the raw YAML {@link String} violates the YAML syntax,
     * {@link io.github.fusionyaml.exceptions.YamlParseFailedException} will be thrown.
     *
     * @param raw The raw YAML {@link String}
     * @return A {@link Map} of {@link String}s and {@link Object}s, parsed from a {@link DefaultParser} object.
     */
    public Map<String, Object> parseYAML(String raw) {
        return parseYAML(raw, DEFAULT_PARSER);
    }

    /**
     * Loads the JSON {@link String} into a {@link YamlObject}. The raw JSON {@link String} will first be
     * converted to a {@link Map} of {@link String}s and {@link Object}s before loading it to a {@link YamlObject}
     * via {@link #fromYaml(Map, YamlParser.YamlType)}.
     * <p>
     * The {@link io.github.fusionyaml.parser.YamlParser.YamlType} is also needed because YAML is saved to
     * a {@link String} or a {@link java.io.File} (etc), the structure of the YAML will be the same as what
     * {@link io.github.fusionyaml.parser.YamlParser.YamlType} is passed in.
     *
     * @param raw The raw JSON {@link String}
     * @param type The {@link io.github.fusionyaml.parser.YamlParser.YamlType} of the YAML
     * @return The {@link YamlObject} loaded from a raw JSON {@link String}
     */
    public YamlObject fromJSON(String raw, YamlParser.YamlType type) {
        Map<String, Object> contents = GSON_DEFAULT.fromJson(raw, MAP_TYPE);
        return fromYaml(contents, type);
    }

    /**
     * Loads the JSON {@link String} into a {@link YamlObject}. The raw JSON {@link String} will first be
     * converted to a {@link Map} of {@link String}s and {@link Object}s before loading it to a {@link YamlObject}
     * via {@link #fromYaml(Map, YamlParser.YamlType)}.
     * <p>
     * The {@link io.github.fusionyaml.parser.YamlParser.YamlType} will be set to a default YAML type, which is
     * map type.
     *
     * @param raw The raw JSON {@link String}
     * @return The {@link YamlObject} loaded from a raw JSON {@link String}
     */
    public YamlObject fromJSON(String raw) {
        return fromJSON(raw, DEFAULT_YAML_TYPE);
    }

    /**
     * Loads a {@link JsonObject} and converts it to {@link YamlObject}. The method also requires a
     * {@link io.github.fusionyaml.parser.YamlParser.YamlType}, which is required because when YAML
     * is saved to disk, the structure of the YAML will be adjusted depending on the
     * {@link io.github.fusionyaml.parser.YamlParser.YamlType}
     *
     * @param object The {@link JsonObject}
     * @param type The {@link io.github.fusionyaml.parser.YamlParser.YamlType}
     * @return The {@link YamlObject} converted from a {@link JsonObject}
     */
    public YamlObject fromJSON(JsonObject object, YamlParser.YamlType type) {
        Map<String, Object> map = GSON_DEFAULT.fromJson(object, MAP_TYPE);
        Map<String, YamlElement> converted = YamlUtils.toMap(map);
        return new YamlObject(converted, type);
    }

    /**
     * Loads a {@link JsonObject} and converts it to {@link YamlObject}. The
     * {@link io.github.fusionyaml.parser.YamlParser.YamlType} will be set to map type, the default type.
     *
     * @param object The {@link JsonObject}
     * @return The {@link YamlObject} converted from a {@link JsonObject}
     */
    public YamlObject fromJSON(JsonObject object) {
        return fromJSON(object, DEFAULT_YAML_TYPE);
    }

    /**
     * Loads a {@link Map} of {@link String}s and {@link Object}s into a {@link YamlObject}. Since it is
     * nearly impossible to infer what {@link io.github.fusionyaml.parser.YamlParser.YamlType} the user
     * wants reading from a {@link Map}, the {@link io.github.fusionyaml.parser.YamlParser.YamlType} is
     * required.
     * <p>
     * The {@link io.github.fusionyaml.parser.YamlParser.YamlType} is useful for knowing what structure
     * will the YAML be written in.
     *
     * @param map The {@link Map} of {@link String}s and {@link Object}s that contain YAML data that
     *            is yet to be loaded into a {@link YamlObject}
     * @param type The {@link io.github.fusionyaml.parser.YamlParser.YamlType}
     * @return The {@link YamlObject} loaded from a {@link Map} of {@link String}s and {@link Object}s
     */
    public YamlObject fromYaml(Map<String, Object> map, YamlParser.YamlType type) {
        Map<String, YamlElement> mem = YamlUtils.toMap(map);
        return new YamlObject(mem, type);
    }

    /**
     * Loads a {@link Map} of {@link String}s and {@link Object}s into a {@link YamlObject}. The
     * {@link io.github.fusionyaml.parser.YamlParser.YamlType} of the {@link YamlObject} will be
     * of map type, the default {@link io.github.fusionyaml.parser.YamlParser.YamlType}.
     *
     * @param map The {@link Map} of {@link String}s and {@link Object}s that contain YAML data that
     *            is yet to be loaded into a {@link YamlObject}
     * @return The {@link YamlObject} loaded from a {@link Map} of {@link String}s and {@link Object}s
     */
    public YamlObject fromYaml(Map<String, Object> map) {
        return this.fromYaml(map, DEFAULT_YAML_TYPE);
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
     * to a {@link io.github.fusionyaml.object.YamlPrimitive} in this object, and this {@link Object} didn't have the
     * {@link TypeAdapter} registered, the {@link ObjectTypeAdapter} will deserialize it to a {@link String}
     *
     * @param element The {@link YamlElement}, which is a serialized form of an object of type
     *                {@link T}
     * @param as The class to deserialize into.
     * @param <T> The class type
     * @return The deserialized {@link YamlElement} of type {@link T}
     * @throws io.github.fusionyaml.exceptions.YamlDeserializationException Thrown when an error occurred while
     * deserializing
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(YamlElement element, Type as) {
        TypeAdapter<T> adapter = getTypeAdapter(classTypeAdapterMap, as);
        if (adapter == null) adapter = new ObjectTypeAdapter<>(this);
        return adapter.deserialize(element, as);
    }

    /**
     * Deserializes a raw YAML {@link String} into an object of type {@link T}. Please note that the {@link String}
     * should contain only one document or {@link io.github.fusionyaml.exceptions.YamlParseFailedException} will
     * be thrown. The method will look for appropriate {@link TypeAdapter}s for the {@link Class} of type {@link T}
     * and then use the {@link TypeAdapter} to deserialize.
     * <p>
     * If no such {@link TypeAdapter} is found, {@link ObjectTypeAdapter} will be used to deserialize the YAML
     * {@link String} into a {@link Class} of type {@link T}. The type adapter will create a new instance of
     * {@link T} without calling a constructor, even the default one.
     * <p>
     * If an error occurred during parsing, {@link io.github.fusionyaml.exceptions.YamlParseFailedException} will
     * be thrown. One of the most common causes of thus exception is invalid YAML syntax, which is detected during
     * mapping.
     * <p>
     * If an error occurred while deserializing, {@link io.github.fusionyaml.exceptions.YamlDeserializationException}
     * will be thrown.
     *
     * @param yaml The raw YAML {@link String}
     * @param as The {@link Class} of type {@link T} to deserialize into
     * @param <T> The type
     * @return The {@link String} YAML deserialized into an {@link Object} of {@link T}
     * @throws io.github.fusionyaml.exceptions.YamlParseFailedException Thrown when a {@link YamlParser}
     * throws an exception
     * @throws io.github.fusionyaml.exceptions.YamlDeserializationException Thrown when an error occurred
     * during deserializing
     */
    public <T> T deserialize(String yaml, Class<T> as) {
        return deserialize(parseYAML(yaml), as);
    }

    /**
     * Deserializes a {@link Map} of {@link String}s and {@link Object}s into an object of type {@link T}.
     * The method will look for the appropriate {@link TypeAdapter} for that object. If an appropriate
     * {@link TypeAdapter} is found, the method will the {@link TypeAdapter} object to deserialize the map.
     * <p>
     * If no appropriate {@link TypeAdapter} is found, {@link ObjectTypeAdapter} will be used instead. The
     * {@link ObjectTypeAdapter} deserializes an object by creating an object instance (of type {@link T}) and
     * initializing the fields depending on the map's entries.
     * <p>
     * In {@link ObjectTypeAdapter}, every entry on the map represents a field. The entry's key will be treated as a
     * field's name and the value will be treated as the field's value. The {@link ObjectTypeAdapter} will also look
     * for a valid {@link TypeAdapter} for the field type and deserialize the value on the map to an {@link Object} of
     * the field's type.
     * <p>
     * If an error occurred while deserializing, {@link io.github.fusionyaml.exceptions.YamlDeserializationException}
     * will be thrown.
     *
     * @param map The {@link Map} containing key-value pairs. Each key represents a path, and each value represents
     *            the value to the path. The value can also be a map, which makes the key of the map a nested path.
     * @param as The class object to deserialize into
     * @param <T> The object type
     * @return The {@link Map} deserialized into an object of type {@link T}
     * @throws io.github.fusionyaml.exceptions.YamlDeserializationException Thrown when an error occurred during
     * deserializing
     */
    public <T> T deserialize(Map<String, Object> map, Class<T> as) {
        Map<String, YamlElement> converted = YamlUtils.toMap(map);
        YamlObject object = new YamlObject(converted);
        return deserialize(object, as);
    }

    /**
     * Converts a {@link YamlObject} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlObject} object to a JSON {@link String}.
     *
     * @param object The {@link YamlObject}
     * @param gson The {@link Gson} instance
     * @return The JSON {@link String}, which is converted from the {@link YamlObject} object.
     */
    public String toJSON(YamlObject object, Gson gson) {
        Map<String, Object> converted = YamlUtils.toMap0(object);
        return gson.toJson(converted);
    }

    /**
     * Converts a {@link YamlObject} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlObject} object to a JSON {@link String}.
     *
     * @param object The {@link YamlObject}
     * @param pretty Whether the method should use {@link Gson}'s pretty printing or not
     * @return The JSON {@link String}, which is converted from the {@link YamlObject} object.
     */
    public String toJSON(YamlObject object, boolean pretty) {
        final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(object, gson);
    }

    /**
     * Converts a {@link YamlObject} to a JSON {@link String}. The method also requires a {@link Gson} instance, which
     * will be used to convert the retrieved map from the {@link YamlObject} object to a JSON {@link String}.
     * The method will use default {@link Gson} to convert the {@link Map} of {@link String}s and {@link Object}s to
     * a JSON string.
     *
     * @param object The {@link YamlObject}
     * @return The JSON {@link String}, which is converted from the {@link YamlObject} object.
     */
    public String toJSON(YamlObject object) {
        return toJSON(object, false);
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will first parse the YAML and convert it
     * to a {@link Map} of {@link String}s and {@link Object}s. Next, the method will use the {@link Gson} instance
     * passed in to convert it to a YAML {@link String}
     *
     * @param yaml The YAML {@link String}
     * @param gson The {@link Gson} object
     * @return The JSON {@link String} converted from a YAML {@link String}
     */
    public String toJSON(String yaml, Gson gson) {
        return gson.toJson(parseYAML(yaml));
    }

    /**
     * Converts a {@link YamlObject} to a {@link JsonObject}
     *
     * @param object The {@link YamlObject}
     * @return The {@link JsonObject} converted from a {@link YamlObject} object
     */
    public JsonObject toJSONObject(YamlObject object) {
        Map<String, Object> map = YamlUtils.toMap0(object);
        return GSON_DEFAULT.toJsonTree(map).getAsJsonObject();
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will first parse the YAML and convert it
     * to a {@link Map} of {@link String}s and {@link Object}s. Next, the method will use the {@link Gson}'s pretty
     * print-enabled object to convert it to a JSON {@link String} if the {@code boolean} passed in was true.
     *
     * @param yaml The YAML {@link String}
     * @param pretty Whether the JSON {@link String} should be prettified or not
     * @return The JSON {@link String} converted from a YAML {@link String}
     */
    public String toJSON(String yaml, boolean pretty) {
        final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(yaml, gson);
    }

    /**
     * Converts a YAML {@link String} to a JSON {@link String}. The method will use the default {@link Gson} object
     * to convert it from a {@link Map} of {@link String}s and {@link Object}s, which is mapped from a
     * {@link DefaultParser}, to a JSON {@link String}.
     * <p>
     * If an error occurred while parsing, {@link io.github.fusionyaml.exceptions.YamlParseFailedException}
     * will be thrown.
     *
     * @param yaml The YAML {@link String}
     * @return The JSON {@link String}, converted from a JSON {@link String}
     */
    public String toJSON(String yaml) {
        return toJSON(yaml, false);
    }


    private static <T> TypeAdapter<T> getTypeAdapter(Map<Type, TypeAdapter> classTypeAdapterMap, Type as) {
        TypeAdapter adapter = null;
        int lpsCount = -1;
        for (Map.Entry entry : classTypeAdapterMap.entrySet()) {
            int lps = ReflectionUtils.lps((Class) as, (Class) entry.getKey(), 0);
            if (lps != -1) {
               if (lpsCount > lps || lpsCount == -1) {
                   lpsCount = lps;
                   adapter = (TypeAdapter<?>) entry.getValue();
               }
            }
        }

        return adapter;
    }

    private static Map<String, Object> toMap(Map<Type, TypeAdapter> classTypeAdapterMap, ObjectTypeAdapter typeAdapter, Object o, Type typeOfO) {
        TypeAdapter adapter = getTypeAdapter(classTypeAdapterMap, o.getClass());
        YamlElement serialized;
        if (adapter != null)
            serialized = adapter.serialize(o, typeOfO);
        else serialized = typeAdapter.serialize(o, typeOfO);
        if (!serialized.isYamlObject()) {
            Map<String, YamlElement> map = new LinkedHashMap<>();
            map.put(o.hashCode() + "", serialized);
            serialized = new YamlObject(map);
        }
        return YamlUtils.toMap0(serialized.getAsYamlObject());
    }

    /**
     * Serializes an {@link Object} into a YAML {@link String}. The method will look for an appropriate
     * {@link TypeAdapter} for the {@link Object}, and use its serialize method to serialize it to a
     * {@link YamlElement}. The {@link YamlElement} will then be converted to a raw YAML {@link String}.
     * <p>
     * If an appropriate {@link TypeAdapter} is found, and its serialized method returned any {@link YamlElement} other
     * than a {@link YamlObject} object, the method will create a {@link YamlObject}. The {@link YamlObject}'s
     * only key-value pair will be set to the {@link Object}'s hashcode as the key and the {@link YamlElement} as
     * the value.
     * <p>
     * If no appropriate {@link TypeAdapter} is found, the method will use an {@link ObjectTypeAdapter}. The
     * {@link ObjectTypeAdapter} will copy the instance fields' data into a {@link Map} of {@link String}s and
     * {@link Object}s and convert it to a {@link YamlObject}.
     *
     * @param o The {@link Object} to serialize
     * @return The serialized {@link Object}
     */
    public String toYAML(Object o) {
        return yaml.dump(toMap(classTypeAdapterMap, OBJECT_TYPE_ADAPTER, o, o.getClass()));
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
        TypeAdapter adapter = getTypeAdapter(o.getClass());
        return adapter.serialize(o, type);
    }

}