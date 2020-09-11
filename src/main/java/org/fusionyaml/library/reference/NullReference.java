package org.fusionyaml.library.reference;

import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlNull;
import org.fusionyaml.library.object.YamlObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a reference to a null value (or to a {@link YamlNull} element)
 */
public final class NullReference implements Reference {
    
    private final String referenceStr;
    private final YamlObject object;
    
    private final List<String> path;
    
    NullReference(LinkedList<String> path, String referenceStr, YamlObject object) {
        this.path = path;
        this.referenceStr = referenceStr;
        this.object = object;
    }
    
    /**
     * @return The referenced {@link YamlElement}
     */
    @Override
    public YamlElement getReferenced() {
        return YamlNull.NULL;
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
        return object;
    }
    
    @Override
    public String toString() {
        return referenceStr;
    }
}
