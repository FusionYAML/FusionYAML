package org.fusionyaml.library.serialization;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.internal.Converter;
import org.fusionyaml.library.object.YamlArray;
import org.fusionyaml.library.object.YamlElement;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

public class CollectionTypeAdapter<T> extends TypeAdapter<Collection<T>> {
    
    private final FusionYAML fusionYAML;
    private final Converter converter = new Converter();
    
    public CollectionTypeAdapter(FusionYAML yaml) {
        fusionYAML = yaml;
    }
    
    @Override
    public YamlElement serialize(Collection<T> obj, Type type) {
        LinkedList<YamlElement> in = new LinkedList<>();
        obj.forEach(e -> in.add(fusionYAML.serialize(e, e.getClass())));
        return new YamlArray(in);
    }

    @Override
    public Collection<T> deserialize(YamlElement element, Type typeOfT) {
        YamlArray list = element.getAsYamlArray();
        Collection<T> collection = new LinkedList<>();
        list.forEach(e -> {
            Object o = converter.toSnakeYAML(e);
            collection.add(fusionYAML.deserialize(e, o.getClass()));
        });
        return collection;
    }
}
