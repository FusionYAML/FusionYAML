package org.fusionyaml.library.object;

/**
 * Represents a null value in YAML
 */
public class YamlNull implements YamlElement {
    
    public static final YamlNull NULL = new YamlNull();
    
    private YamlNull() {
    }
    
    @Override
    public int hashCode() {
        return YamlNull.class.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof YamlNull;
    }
    
    /**
     * Copies all of the elements and the children contained
     * herein
     *
     * @return A copy of this {@link YamlElement}
     */
    @Override
    public YamlNull deepCopy() {
        // null values aren't deep copied
        return NULL;
    }
}
