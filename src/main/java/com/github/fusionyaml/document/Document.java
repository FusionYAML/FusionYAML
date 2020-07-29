package com.github.fusionyaml.document;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlObject;

import java.util.ArrayList;
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

    static Document newInstance(YamlObject object, List<YamlComment> comments, FusionYAML yaml) {
        return new DocumentImpl(object, comments, yaml);
    }

    static Document newInstance(YamlObject object, FusionYAML yaml) {
        return newInstance(object, new ArrayList<>(), yaml);
    }

}
