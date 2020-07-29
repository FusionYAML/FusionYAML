package com.github.fusionyaml.document;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlObject;

import java.util.List;

/**
 * Represents one YAML document.
 */
public interface Document {

    /**
     * @return A {@link YamlObject} containing all key and value
     * pairs
     */
    YamlObject toYamlObject();

    /**
     * @return The comments in the document
     */
    List<YamlComment> getComments();

    FusionYAML getFusionYAML();

    @Override
    String toString();

}
