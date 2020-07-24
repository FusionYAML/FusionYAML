package io.github.fusionyaml.serialization;

import com.google.common.reflect.TypeToken;
import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlArray;
import io.github.fusionyaml.utils.YamlUtils;

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
            System.out.println(e + "_ IN LIST");
            Object o = YamlUtils.toObject0(e);
            collection.add(fusionYAML.deserialize(e, o.getClass()));
        });
        return collection;
    }
}
