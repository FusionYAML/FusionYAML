package org.fusionyaml.library.reference;

import org.fusionyaml.library.configurations.YamlConfiguration;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of the element reference class
 */
public final class ElementReference implements Reference {
    
    private final String referenceString;
    private final YamlConfiguration configuration;
    private final LinkedList<String> path;
    private YamlElement referenced;
    
    ElementReference(LinkedList<String> path, String referenceString, YamlConfiguration config) {
        this.path = path;
        this.referenceString = referenceString;
        this.configuration = config;
    }
    
    /**
     * @return The referenced {@link YamlElement}
     */
    @Override
    public YamlElement getReferenced() {
        if (referenced == null) {
            referenced = configuration.getElement(getPath());
        }
        return referenced;
    }
    
    /**
     * Gets the path the element is present. Every index in the {@link List}
     * represents a shift downward to the nested path where the previous index
     * is the parent.
     *
     * @return The path
     */
    @Override
    public List<String> getPath() {
        return new LinkedList<>(path);
    }
    
    /**
     * @return The whole document (or part thereof). The returned {@link YamlObject}
     * should contain the referenced {@link YamlElement} and the reference itself.
     */
    @Override
    public YamlObject getSearchedObject() {
        return configuration.toYamlObject();
    }
    
    @Override
    public String toString() {
        return referenceString;
    }
    
}
