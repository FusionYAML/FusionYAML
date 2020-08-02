package com.github.fusionyaml;

import com.github.fusionyaml.object.*;
import com.github.fusionyaml.serialization.TypeAdapter;
import org.yaml.snakeyaml.DumperOptions;

import java.util.*;

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
            return toDumpableMap(element.getAsYamlObject());
        else return null;
    }

    public static YamlElement toElement(Object o) {
        if (o == null) return YamlNull.NULL;
        if (o instanceof YamlElement)
            return (YamlElement) o;
        if (isPrimitive(o))
            return new YamlPrimitive(o);
        if (o instanceof List) {
            YamlArray list = new YamlArray();
            List<Object> objList = (List<Object>) o;
            objList.forEach(b -> {
                list.add($DataBridge.toElement(b));
            });
            return list;
        }
        if (o instanceof Map) {
            Map map = (Map) o;
            return toYamlObject(toStringMap(map));
        } else return YamlNull.NULL;
    }

    public static Map<String, Object> toStringMap(Map map) {
        Map<String, Object> mem = new LinkedHashMap<>();
        map.forEach((k, v) -> mem.put(k.toString(), v));
        return mem;
    }

    public static YamlObject toYamlObject0(Map<String, Object> map) {
        return toYamlObject(map);
    }

    public static Map<String, YamlElement> toYamlElementMap(YamlObject object) {
        Map<String, YamlElement> map = new LinkedHashMap<>();
        object.forEach(map::put);
        return map;
    }

    public static <T> T toObject(YamlElement e) {
        if (e instanceof YamlPrimitive)
            return (T) e.getAsYamlPrimitive().getValue();
        else if (e instanceof YamlArray)
            return (T) toObjectList(e.getAsYamlArray().getList());
        else if (e instanceof YamlObject)
            return (T) $DataBridge.toDumpableMap(e.getAsYamlObject());
        else return null;
    }

    public static boolean isPrimitive(Object o) {
        return o instanceof Number || o instanceof Character || o instanceof Boolean || o instanceof String;
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
    public static Map<String, Object> toDumpableMap(YamlObject object) {
        Map<String, Object> newMap = new LinkedHashMap<>();
        object.forEach((k, v) -> newMap.put(k, toDumpableObject(v)));
        return newMap;
    }

    public static Map<String, Object> toDumpableMap(YamlObject object, FusionYAML fusionYAML) {
        return toDumpableMap(removeNullIfEnabled(object, fusionYAML));
    }

    public static YamlObject removeNullIfEnabled(YamlObject object, FusionYAML yaml) {
        if (!yaml.getYamlOptions().isExcludeNullVals()) return object;
        return removeNull(object);
    }

    private static YamlObject removeNull(YamlObject object) {
        YamlObject obj = new YamlObject();
        for (String key : object.keySet()) {
            YamlElement element = object.get(key);
            System.out.println(element);
            if (element != null && !element.isYamlNull()) {
                if (element.isYamlObject()) {
                    YamlObject removed = removeNull(element.getAsYamlObject());
                    if (removed.size() != 0)
                        obj.set(key, removed);
                } else obj.set(key, element);

            }
        }
        return obj;
    }

    public static DumperOptions getDumperOptions(YamlOptions options) {
        return options.dumperOptions();
    }

    public static YamlObject toYamlObject(Map<String, Object> map) {
        YamlObject object = new YamlObject();
        map.forEach((k, v) -> {
            object.set(k, $DataBridge.toElement(v));
        });
        return object;
    }

    public static YamlObject toYamlObject(Map<String, Object> map, FusionYAML fusionYAML) {
        return removeNullIfEnabled(toYamlObject(map), fusionYAML);
    }

    public static List<Object> toObjectList(Collection<YamlElement> list) {
        List<Object> l = new LinkedList<>();
        list.forEach(e -> l.add(toObject(e)));
        return l;
    }

    public static YamlArray toYamlArray(List<Object> dumpable) {
        List<YamlElement> elements = new LinkedList<>();
        dumpable.forEach(o -> elements.add(toElement(o)));
        return new YamlArray(elements);
    }

}
