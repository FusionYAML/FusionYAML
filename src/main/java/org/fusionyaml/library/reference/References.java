package org.fusionyaml.library.reference;

import com.google.common.base.Splitter;
import org.fusionyaml.library.configurations.YamlConfiguration;
import org.fusionyaml.library.object.YamlArray;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A utility class for references
 */
public class References {
    
    private References() {
    }
    
    /**
     * Gets all of the references present in the {@link YamlObject}
     * passed in
     *
     * @param object The {@link YamlObject}
     * @return The {@link List} pf references loaded
     */
    public static List<Reference> getReferences(YamlObject object) {
        return loadReferences(object, object);
    }
    
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
        referenceToPathList(referenceStr, path);
        return newReference(referenceStr, path, search);
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
    public static Reference newReference(LinkedList<String> path, YamlObject search) {
        String referenceStr = referencePathToString(path);
        return newReference(referenceStr, path, search);
    }
    
    private static Reference newReference(String referenceStr, LinkedList<String> path,
                                          YamlObject search) {
        if (!isReference(referenceStr)) return InvalidReference.INSTANCE;
        YamlConfiguration config = new YamlConfiguration(search);
        YamlElement element = (search == null) ? null : config.getElement(path);
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
    
    /**
     * Substitutes the references with the value in the {@link YamlObject}
     * passed in.
     *
     * @param object The {@link YamlObject}
     * @param search The {@link YamlObject} where the referenced values live
     */
    public static void substituteReferences(YamlObject object, YamlObject search) {
        object.forEach((k, v) -> {
            if (v.isYamlPrimitive()) {
                String referenceStr = v.getAsYamlPrimitive().getValue().toString();
                if (isReference(referenceStr)) {
                    object.set(k, parseReference(referenceStr, search).getReferenced());
                }
            } else if (v.isYamlObject()) {
                substituteReferences(v.getAsYamlObject(), search);
                //object.toLinkedMap().put(k, v.getAsYamlObject());
            } else if (v.isYamlArray()) {
                substituteReferencesArray(v.getAsYamlArray(), search);
            }
        });
    }
    
    /**
     * Substitutes the references with the value in the {@link YamlArray}
     * passed in.
     *
     * @param array  The {@link YamlArray}
     * @param object The {@link YamlObject} where the referenced values live
     */
    public static void substituteReferencesArray(YamlArray array, YamlObject object) {
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
    
    /**
     * Gets the {@link Reference} found in the path given. Depending on
     * the {@link Reference}, different types of {@link Reference} may
     * be returned.
     *
     * @param object The {@link YamlObject}
     * @param path   The path to the reference
     * @return The reference
     */
    public static Reference getReference(YamlObject object, List<String> path) {
        YamlElement found = new YamlConfiguration(object).getElement(path);
        if (!found.isYamlPrimitive()) return InvalidReference.INSTANCE;
        return parseReference(found.getAsString(), object);
    }
    
    /**
     * Gets the {@link Reference} found in the path given. Depending on
     * the {@link Reference}, different types of {@link Reference} may
     * be returned.
     *
     * @param object The {@link YamlObject}
     * @param key    The path to the reference
     * @return The reference
     */
    public static Reference getReference(YamlObject object, String key) {
        return getReference(object, Collections.singletonList(key));
    }
    
    /**
     * Gets the {@link YamlElement} in the path given. If the
     * {@link YamlElement} is a reference, the referenced value will be
     * returned.
     *
     * @param object A {@link YamlObject}
     * @param path   The path to the element
     * @return The element found
     */
    public static YamlElement getElement(YamlObject object, List<String> path) {
        YamlElement element = new YamlConfiguration(object).getElement(path);
        Reference reference = getReference(object, path);
        if (reference.equals(InvalidReference.INSTANCE))
            return element;
        YamlElement copy = reference.getReferenced().deepCopy();
        YamlObject objCopy = object.deepCopy();
        if (copy.isYamlObject()) substituteReferences(copy.getAsYamlObject(), objCopy);
        else if (copy.isYamlArray()) substituteReferencesArray(copy.getAsYamlArray(), objCopy);
        return copy;
    }
    
    /**
     * Gets the {@link YamlElement} in the path given. If the
     * {@link YamlElement} is a reference, the referenced value will be
     * returned.
     *
     * @param object    A {@link YamlObject}
     * @param path      The path to the element
     * @param separator The separator, where each usage signals a descent
     *                  under the path mentioned before it
     * @return The element found
     */
    public static YamlElement getElement(YamlObject object, String path, char separator) {
        return getElement(object, Splitter.on(separator).splitToList(path));
    }
    
    /**
     * Gets the {@link YamlElement} in the path given. If the
     * {@link YamlElement} is a reference, the referenced value will be
     * returned.
     *
     * @param object A {@link YamlObject}
     * @param path   The path to the element
     * @return The element found
     */
    public static YamlElement getElement(YamlObject object, String path) {
        return getElement(object, Collections.singletonList(path));
    }
    
    /**
     * Copies the element and substitutes the references with the
     * element in the copied element.
     *
     * @param element The element
     * @param search  The root (or any {@link YamlObject} which contains the
     *                referenced info)
     * @return A new {@link YamlElement} having their references replaced
     * with the referenced value
     */
    public static YamlElement copyAndSubstituteReferences(YamlElement element,
                                                          YamlObject search) {
        YamlElement copy = element.deepCopy();
        YamlObject searchCopy = search.deepCopy();
        if (copy.isYamlArray()) substituteReferencesArray(copy.getAsYamlArray(), searchCopy);
        if (copy.isYamlObject()) substituteReferences(copy.getAsYamlObject(), searchCopy);
        return copy;
    }
    
    /**
     * Copies the yaml object given and substitutes the references
     * present within.
     *
     * @param object The object
     * @return A new {@link YamlObject} having their references replaced
     * with the referenced value
     */
    public static YamlObject copyAndSubstituteReferences(YamlObject object) {
        return (YamlObject) copyAndSubstituteReferences(object, object);
    }
    
    /**
     * Sets the reference in the given path in the {@link YamlObject}
     * given in.
     * <p>
     * This is equivalent to setting the references manually using the
     * syntax (i.e.: {@code object.set(pathHere, "$referenced")}
     *
     * @param object    The {@link YamlObject}
     * @param path      The path
     * @param reference The reference
     */
    public static void setReference(YamlObject object, List<String> path,
                                    Reference reference) {
        new YamlConfiguration(object).set(path, reference.toString());
    }
    
    /**
     * Sets the reference in the given path in the {@link YamlObject}
     * given in.
     * <p>
     * This is equivalent to setting the references manually using the
     * syntax (i.e.: {@code object.set(pathHere, "$referenced")}
     *
     * @param object    The {@link YamlObject}
     * @param path      The path
     * @param separator The path separator
     * @param reference The reference
     */
    public static void setReference(YamlObject object, String path, char separator,
                                    Reference reference) {
        setReference(object, Splitter.on(separator).splitToList(path), reference);
    }
    
    /**
     * Sets the reference in the given path in the {@link YamlObject}
     * given in.
     * <p>
     * This is equivalent to setting the references manually using the
     * syntax (i.e.: {@code object.set(pathHere, "$referenced")}
     *
     * @param object    The {@link YamlObject}
     * @param path      The path
     * @param reference The reference
     */
    public static void setReference(YamlObject object, String path, Reference reference) {
        setReference(object, Collections.singletonList(path), reference);
    }
    
    /********************************
     PRIVATE METHODS
     ********************************/
    
    private static void referenceToPathList(String src, List<String> destination) {
        char[] array = src.trim().substring(1).toCharArray();
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
    
    private static List<Reference> loadReferences(YamlElement element, YamlObject root) {
        List<Reference> loaded = new ArrayList<>();
        if (element.isYamlObject())
            element.getAsYamlObject().forEach((k, v) -> loaded.addAll(loadReferences(v, root)));
        else if (element.isYamlArray())
            element.getAsYamlArray().forEach(e -> loaded.addAll(loadReferences(e, root)));
        else if (element.isYamlPrimitive()) {
            String text = element.getAsString();
            if (References.isReference(text))
                loaded.add(References.parseReference(text, root));
        }
        return loaded;
    }
    
    
}
