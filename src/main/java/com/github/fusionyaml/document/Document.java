package com.github.fusionyaml.document;

import com.github.fusionyaml.object.YamlObject;

import java.util.List;

/**
 * Represents one YAML document.
 */
public interface Document {

    YamlObject getObject();

    /**
     * @return The comments in the document
     */
    List<YamlComment> getComments();

    @Override
    String toString();

}
