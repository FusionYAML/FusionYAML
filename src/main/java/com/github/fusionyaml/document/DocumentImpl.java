package com.github.fusionyaml.document;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlObject;

import java.util.List;

class DocumentImpl implements Document {

    private final YamlObject object;
    private final List<YamlComment> comments;
    private final FusionYAML yaml;

    DocumentImpl(YamlObject object, List<YamlComment> comments, FusionYAML yaml) {
        this.object = object;
        this.comments = comments;
        this.yaml = yaml;
    }

    /**
     * @return A {@link YamlObject} containing all key and value
     * pairs
     */
    @Override
    public YamlObject toYamlObject() {
        return object;
    }

    /**
     * @return The comments in the document
     */
    @Override
    public List<YamlComment> getComments() {
        return comments;
    }

    @Override
    public FusionYAML getFusionYAML() {
        return yaml;
    }
}
