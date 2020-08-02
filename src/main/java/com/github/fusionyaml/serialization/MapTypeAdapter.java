package com.github.fusionyaml.serialization;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlObject;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapTypeAdapter<V> extends TypeAdapter<Map<String, V>> {

    public MapTypeAdapter(FusionYAML yaml) {
        super(yaml);
    }

    public MapTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public YamlElement serialize(Map<String, V> obj, Type typeOfV) {
        YamlObject o = new YamlObject();
        if (obj.values().size() == 0) return o;
        YamlObject object = new YamlObject();
        obj.forEach((k, v) -> object.set(k, fusionYAML.serialize(v, v.getClass())));
        return object;
    }

    @Override
    public Map<String, V> deserialize(YamlElement element, Type type) {
        YamlObject object = element.getAsYamlObject();
        Map<String, V> map = new LinkedHashMap<>();
        object.forEach((k, v) -> map.put(k, fusionYAML.deserialize(v, type)));
        return map;
    }

}
