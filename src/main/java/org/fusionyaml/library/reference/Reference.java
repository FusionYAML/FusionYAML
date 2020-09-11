package org.fusionyaml.library.reference;

import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.util.List;

/**
 * A feature exclusive to the FusionYAML library where
 * {@link org.fusionyaml.library.object.YamlElement} are referenced.
 * This functions similar to anchors, except that it has a different
 * syntax and doesn't require the targeted {@link org.fusionyaml.library.object.YamlElement}
 * to be marked.
 * <p>
 * The reference syntax is simple: {@code @path.to.referenced}
 */
public interface Reference {
    
    /**
     * @return The referenced {@link YamlElement}
     */
    YamlElement getReferenced();
    
    /**
     * Gets the path the element is present. Every index in the {@link List}
     * represents a shift downward to the nested path where the previous index
     * is the parent.
     *
     * @return The path
     */
    List<String> getPath();
    
    /**
     * @return The whole document (or part thereof). The returned {@link YamlObject}
     * should contain the referenced {@link YamlElement} and the reference itself.
     */
    YamlObject getSearchedObject();
    
    /**
     * @return The reference text
     */
    String toString();
    
}
