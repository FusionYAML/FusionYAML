package com.github.fusionyaml.serialization;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlArray;
import com.github.fusionyaml.object.YamlElement;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

public class CollectionTypeAdapter<T> extends TypeAdapter<Collection<T>>  {

    public CollectionTypeAdapter(FusionYAML yaml) {
        super(yaml);
    }

    public CollectionTypeAdapter() {
        this(new FusionYAML());
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
            Object o = $DataBridge.toObject(e);
            collection.add(fusionYAML.deserialize(e, o.getClass()));
        });
        return collection;
    }
}
