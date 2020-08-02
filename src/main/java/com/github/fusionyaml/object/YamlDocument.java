package com.github.fusionyaml.object;

import com.google.common.base.Splitter;

import java.util.Collections;
import java.util.List;

public class YamlDocument implements YamlElement {

    private final YamlElement element;

    public YamlDocument(YamlObject object) {
        this.element = object;
    }

    public YamlDocument(YamlArray array) {
        this.element = array;
    }

    @Override
    public YamlObject getAsYamlObject() {
        return element.getAsYamlObject();
    }

    @Override
    public YamlArray getAsYamlArray() {
        return element.getAsYamlArray();
    }

    public YamlDocument add(String path, String value) {
        return add(Collections.singletonList(path), new YamlPrimitive(value));
    }

    public YamlDocument add(String path, Number value) {
        return add(Collections.singletonList(path), new YamlPrimitive(value));
    }

    public YamlDocument add(String path, Boolean value) {
        return add(Collections.singletonList(path), new YamlPrimitive(value));
    }

    public YamlDocument add(String path, YamlElement value) {
        return add(Collections.singletonList(path), value);
    }

    public YamlDocument add(String path, char separator, YamlElement value) {
        return add(Splitter.on(separator).splitToList(path), value);
    }

    public YamlDocument add(List<String> paths, YamlElement value) {
        if (element instanceof YamlObject)
            element.getAsYamlObject().set(paths, value);
        else element.getAsYamlArray().add(new YamlObject().set(paths, value));
        return this;
    }

}
