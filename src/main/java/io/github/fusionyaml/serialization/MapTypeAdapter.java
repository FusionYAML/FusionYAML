package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;

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
        Map<String, YamlElement> map = new LinkedHashMap<>();
        obj.forEach((k, v) -> map.put(k, fusionYAML.serialize(v, typeOfV)));
        return new YamlObject(map, fusionYAML);
    }

    @Override
    public Map<String, V> deserialize(YamlElement element, Type type) {
        YamlObject object = element.getAsYamlObject();
        Map<String, V> map = new LinkedHashMap<>();
        object.getMap().forEach((k, v) -> map.put(k, fusionYAML.deserialize(v, type)));
        return map;
    }

}
