package org.fusionyaml.library.reference;

import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.util.List;

public final class InvalidReference implements Reference {
    
    public static final InvalidReference INSTANCE = new InvalidReference();
    
    private InvalidReference() {
    }
    
    /**
     * @return The referenced {@link YamlElement}
     */
    @Override
    public YamlElement getReferenced() {
        throw new UnsupportedOperationException(toString());
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
        throw new UnsupportedOperationException(toString());
    }
    
    /**
     * @return The whole document (or part thereof). The returned {@link YamlObject}
     * should contain the referenced {@link YamlElement} and the reference itself.
     */
    @Override
    public YamlObject getSearchedObject() {
        throw new UnsupportedOperationException(toString());
    }
    
    @Override
    public String toString() {
        return "$";
    }
    
}
