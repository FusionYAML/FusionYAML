package org.fusionyaml.library.internal;


import org.fusionyaml.library.YamlOptions;
import org.fusionyaml.library.object.*;
import org.fusionyaml.library.serialization.TypeAdapter;
import org.yaml.snakeyaml.DumperOptions;

import java.util.*;

/**
 * Allows you to convert Snakeyaml style objects to FusionYAML style
 * objects
 */
public final class Converter {
    
    private static boolean isYamlPrimitive(Object o) {
        return o instanceof Number || o instanceof Character || o instanceof Boolean || o instanceof String;
    }
    
    @SuppressWarnings("all")
    private static Map<String, Object> toStrMap(Map map) {
        Map<String, Object> newMap = new LinkedHashMap<>();
        map.forEach((k, v) -> newMap.put(k.toString(), v));
        return newMap;
    }
    
    /**
     * This is, by no means, equivalent to deserializing the element using its
     * {@link TypeAdapter}. The returned object will be able to be dumped using
     * snakeyaml's library
     *
     * @param element The element to be converted
     * @return An object that could be dumped using snakeyaml's library
     */
    public Object toSnakeYAML(YamlElement element) {
        if (element == null) return null;
        else if (element.isYamlPrimitive()) return element.getAsYamlPrimitive().getValue();
        else if (element.isYamlArray()) return toSnakeYAMLList(element.getAsYamlArray().getList());
        else if (element.isYamlObject()) {
            Map<String, Object> map = new LinkedHashMap<>();
            element.getAsYamlObject().forEach((k, v) -> map.put(k, toSnakeYAML(v)));
            return map;
        }
        return null;
    }
    
    /**
     * This is, by no means, equivalent to serializing the element using its
     * {@link TypeAdapter}. The returned {@link YamlElement} is converted from
     * an object that can be dumped by snakeyaml.
     *
     * @param snakeyaml The object that can be dumped with snakeyaml
     * @return An element
     */
    @SuppressWarnings("all")
    public YamlElement toElement(Object snakeyaml) {
        if (snakeyaml == null) return YamlNull.NULL;
        if (snakeyaml instanceof YamlElement) return (YamlElement) snakeyaml;
        if (isYamlPrimitive(snakeyaml)) return new YamlPrimitive(snakeyaml);
        if (snakeyaml instanceof List) return new YamlArray(toListOfElements((List<Object>) snakeyaml));
        if (snakeyaml instanceof Map) {
            YamlObject object = new YamlObject();
            Map<String, YamlElement> elementMap = toMapOfElements(toStrMap((Map) snakeyaml));
            elementMap.forEach(object::set);
            return object;
        }
        return YamlNull.NULL;
    }
    
    /**
     * Converts the {@link YamlOptions} to {@link DumperOptions}
     *
     * @param options The yaml options
     * @return A {@link DumperOptions} object converted from the
     * {@link YamlOptions} object
     */
    public DumperOptions toDumperOptions(YamlOptions options) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setExplicitStart(false);
        dumperOptions.setExplicitEnd(false);
        dumperOptions.setDefaultFlowStyle(options.getFlowStyle());
        dumperOptions.setAllowUnicode(options.isAllowUnicode());
        dumperOptions.setCanonical(options.isCanonical());
        dumperOptions.setDefaultScalarStyle(options.getScalarStyle());
        dumperOptions.setIndent(options.getIndent());
        dumperOptions.setMaxSimpleKeyLength(options.getMaxKeyLength());
        dumperOptions.setLineBreak(options.getLineBreak());
        dumperOptions.setNonPrintableStyle(options.getNonPrintableStyle());
        dumperOptions.setPrettyFlow(options.isPrettyFlow());
        dumperOptions.setSplitLines(options.isSplitLines());
        dumperOptions.setVersion(options.getVersion());
        dumperOptions.setTimeZone(options.getTimeZone());
        dumperOptions.setWidth(options.getWidth());
        return dumperOptions;
    }
    
    /**
     * Converts the {@link Collection} of {@link Object} (representative of a snakeyaml
     * yaml array) to a {@link Collection} of {@link YamlElement}s
     *
     * @param objects The {@link Collection} of objects
     * @return The {@link Collection} of {@link YamlElement}s
     */
    public Collection<YamlElement> toListOfElements(Collection<Object> objects) {
        List<YamlElement> list = new LinkedList<>();
        objects.forEach(obj -> list.add(toElement(obj)));
        return list;
    }
    
    /**
     * Converts the {@link Collection} of {@link YamlElement} to a {@link Collection}
     * of {@link Object} (representative of a snakeyaml yaml array)
     *
     * @param elements The {@link Collection} of {@link YamlElement}s
     * @return The {@link Collection} of {@link Object}
     */
    public List<Object> toSnakeYAMLList(Collection<YamlElement> elements) {
        List<Object> objects = new LinkedList<>();
        elements.forEach(this::toSnakeYAML);
        return objects;
    }
    
    /**
     * Converts the {@link Map} of {@link String}s and {@link Object}s (representative of
     * a yaml mapping in snakeyaml) to a {@link Map} of {@link String} and {@link YamlElement}s
     *
     * @param map The map of {@link String}s and {@link Object}s
     * @return The map of {@link String}s and {@link YamlElement}s
     */
    public Map<String, YamlElement> toMapOfElements(Map<String, Object> map) {
        Map<String, YamlElement> stringYamlElementMap = new LinkedHashMap<>();
        map.forEach((k, v) -> stringYamlElementMap.put(k, toElement(v)));
        return stringYamlElementMap;
    }
    
    /**
     * Inverse function of {@link #toMapOfElements(Map)}
     *
     * @param map The map to convert
     * @return The map that is converted from the other map passed in
     */
    public Map<String, Object> toSnakeYAMLMap(Map<String, YamlElement> map) {
        Map<String, Object> stringObjectMap = new LinkedHashMap<>();
        map.forEach((k, v) -> stringObjectMap.put(k, toSnakeYAML(v)));
        return stringObjectMap;
    }
    
}
