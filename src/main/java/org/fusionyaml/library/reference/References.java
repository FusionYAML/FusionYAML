package org.fusionyaml.library.reference;

import org.fusionyaml.library.configurations.YamlConfiguration;
import org.fusionyaml.library.object.YamlArray;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.util.LinkedList;
import java.util.List;

/**
 * A utility class for references
 */
public class References {
    
    /**
     * Parses the reference given the reference string and {@link YamlObject}
     * passed in. It is best to pass in the {@link YamlObject} document.
     *
     * @param referenceStr The reference string
     * @param search       The {@link YamlObject} that contains the referenced
     * @return An instance of a {@link Reference} with type differing depending on
     * the reference.
     */
    public static Reference parseReference(String referenceStr, YamlObject search) {
        if (referenceStr == null || search == null) return InvalidReference.INSTANCE;
        LinkedList<String> path = new LinkedList<>();
        referenceToPathList(referenceStr.trim().substring(1), path);
        return createReference(referenceStr, path, search);
    }
    
    /**
     * Creates a reference given the referenced path and the {@link YamlObject}.
     * It is best to pass the {@link YamlObject} document.
     *
     * @param path   The reference path
     * @param search The {@link YamlObject} that contains the referenced {@link YamlElement}
     * @return An instance of a {@link Reference} with type differing depending on the
     * reference.
     */
    public static Reference createReference(LinkedList<String> path, YamlObject search) {
        String referenceStr = referencePathToString(path);
        return createReference(referenceStr, path, search);
    }
    
    private static Reference createReference(String referenceStr, LinkedList<String> path,
                                             YamlObject search) {
        if (!isReference(referenceStr)) return InvalidReference.INSTANCE;
        YamlConfiguration config = new YamlConfiguration(search);
        YamlElement element = (search == null) ? null : config.getElement(path);
        System.out.println(path);
        if (element == null) return new NullReference(path, referenceStr, search);
        if (element.isYamlNull()) return new NullReference(path, referenceStr, search);
        return new ElementReference(path, referenceStr, config);
    }
    
    /**
     * Checks if the string passed in is a reference
     *
     * @param str The string
     * @return Whether the string passed in is a reference
     */
    public static boolean isReference(String str) {
        if (str == null) return false;
        String trimmed = str.trim();
        return trimmed.length() >= 1 && trimmed.startsWith("$")
                && !trimmed.contains(" ");
    }
    
    /**
     * Substitutes references with their value in the given
     * {@link YamlObject}
     *
     * @param object The object
     */
    public static void substituteReferences(YamlObject object) {
        substituteReferences(object, object);
    }
    
    private static void substituteReferences(YamlObject object, YamlObject root) {
        object.forEach((k, v) -> {
            if (v.isYamlPrimitive()) {
                String referenceStr = v.getAsYamlPrimitive().getValue().toString();
                if (isReference(referenceStr)) {
                    object.set(k, parseReference(referenceStr, root).getReferenced());
                }
            } else if (v.isYamlObject()) {
                substituteReferences(v.getAsYamlObject(), root);
                object.toLinkedMap().put(k, v.getAsYamlObject());
            } else if (v.isYamlArray()) {
                substituteReferencesArray(v.getAsYamlArray(), root);
            }
        });
    }
    
    private static void substituteReferencesArray(YamlArray array, YamlObject object) {
        for (int i = 0; i < array.size(); i++) {
            YamlElement element = array.get(i);
            if (element.isYamlPrimitive()) {
                String value = element.getAsYamlPrimitive().getAsString();
                if (isReference(value))
                    array.set(i, parseReference(value, object).getReferenced());
            } else if (element.isYamlObject()) substituteReferences(element.getAsYamlObject());
            else if (element.isYamlArray()) substituteReferencesArray(element.getAsYamlArray(), object);
        }
    }
    
    private static void referenceToPathList(String src, List<String> destination) {
        char[] array = src.trim().toCharArray();
        StringBuilder pathBuilder = new StringBuilder();
        for (char character : array) {
            if (character != '.') pathBuilder.append(character);
            else {
                destination.add(pathBuilder.toString());
                pathBuilder = new StringBuilder();
            }
        }
        destination.add(pathBuilder.toString());
    }
    
    private static String referencePathToString(List<String> path) {
        StringBuilder pathBuilder = new StringBuilder("$");
        path.forEach(e -> {
            pathBuilder.append(e).append(".");
        });
        String str = pathBuilder.toString();
        return str.substring(0, str.length() - 1);
    }
    
}
