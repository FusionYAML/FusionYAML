package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapTypeAdapter<V> extends TypeAdapter<Map<String, V>> {

    private final FusionYAML fusionYAML;

    public MapTypeAdapter(FusionYAML fusionYAML) {
        this.fusionYAML = fusionYAML;
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
