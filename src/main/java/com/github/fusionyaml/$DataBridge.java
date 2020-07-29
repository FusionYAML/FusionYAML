package com.github.fusionyaml;

import com.github.fusionyaml.object.*;
import com.github.fusionyaml.serialization.TypeAdapter;
import org.yaml.snakeyaml.DumperOptions;

import java.util.*;

import static com.github.fusionyaml.utils.YamlUtils.*;

/**
 * This class is not intended for public usage.
 */
public class $DataBridge {

    /**
     * This is, by no means, equivalent to deserializing the element using its
     * {@link TypeAdapter}. The returned object will be able to be dumped using
     * snakeyaml's library
     *
     * @param element The element to be converted
     * @return An object that could be dumped using smakeyaml's library
     */
    public static Object toDumpableObject(YamlElement element) {
        if (element == null) return null;
        if (element.isYamlPrimitive()) {
            YamlPrimitive primitive = element.getAsYamlPrimitive();
            if (primitive.isNumber())
                return primitive.getAsNumber();
            else if (primitive.isCharacter())
                return primitive.getAsChar();
            else if (primitive.isBoolean())
                return primitive.getAsBoolean();
            else
                return primitive.getAsString();
        } else if (element.isYamlArray())
            return toDumpableList(((YamlArray) element).getList());
        else if (element.isYamlObject())
            return toDumpableMap(element.getAsYamlObject().getMap());
        else return null;
    }

    public static YamlElement toElement(Object o) {
        if (o == null) return YamlNull.NULL;
        if (o instanceof YamlElement)
            return (YamlElement) o;
        if (isPrimitive(o))
            return new YamlPrimitive(o);
        if (o instanceof List) {
            return toYamlList((List<Object>) o);
        }
        if (o instanceof Map) {
            return new YamlObject(toMap(toStrMap((Map) o)));
        } else return YamlNull.NULL;
    }

    /**
     * This is, by no means, equivalent to deserializing the element by calling its
     * appropriate {@link TypeAdapter}. The returned object will be able to be dumped
     * using snakeyaml's library.
     *
     * @param collection A {@link Collection} of {@link YamlElement}
     * @return A dumpable list
     */
    public static List<Object> toDumpableList(Collection<YamlElement> collection) {
        List<Object> list = new LinkedList<>();
        collection.forEach(c -> list.add(toDumpableObject(c)));
        return list;
    }

    /**
     * @return A map that could be directly dumped by using snakeyaml's library
     */
    public static Map<String, Object> toDumpableMap(Map<String, YamlElement> map) {
        Map<String, Object> newMap = new LinkedHashMap<>();
        map.forEach((k, v) -> newMap.put(k, toDumpableObject(v)));
        return newMap;
    }

    public static DumperOptions getDumperOptions(YamlOptions options) {
        return options.dumperOptions();
    }

}
