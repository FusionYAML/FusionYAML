package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlList;

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
        obj.forEach(e -> in.add(fusionYAML.serialize(e, type)));
        return new YamlList(in);
    }

    @Override
    public Collection<T> deserialize(YamlElement element, Type typeOfT) {
        YamlList list = element.getAsYamlList();
        Collection<T> collection = new LinkedList<>();
        list.getList().forEach(e -> {
            collection.add(fusionYAML.deserialize(e, typeOfT));
        });
        return collection;
    }
}
