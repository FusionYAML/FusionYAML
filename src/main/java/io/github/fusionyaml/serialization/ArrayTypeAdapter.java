package io.github.fusionyaml.serialization;

import com.google.common.base.CharMatcher;
import com.google.common.reflect.TypeToken;
import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.object.YamlArray;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.utils.YamlUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * A default type adapter for all arrays. Both {@link #serialize(Object[], Type)} and
 * {@link #deserialize(YamlElement, Type)} methods functions similarly to those in the
 * respective {@link CollectionTypeAdapter} classes. The arrays will be treated as
 * a {@link java.util.Collection} of {@link T}.
 *
 * @param <T> The component type, but not the array itself.
 *           Instead of passing {@link T}[] as the generic type,
 *           it is recommended to pass {@link T}.
 */
public class ArrayTypeAdapter<T> extends TypeAdapter<T[]> {


    public ArrayTypeAdapter(FusionYAML fusionYAML) {
        super(fusionYAML);
    }

    public ArrayTypeAdapter() {
        this(new FusionYAML());
    }

    @Override
    public YamlElement serialize(T[] obj, Type typeOfT) {
        List<YamlElement> elementList = new LinkedList<>();
        for (T t : obj) {
            if (t.getClass().isArray())
                elementList.add(serialize((T[]) t, typeOfT));
            else elementList.add(fusionYAML.serialize(t, typeOfT));
        }

        return new YamlArray(elementList);
    }

    @Override
    public T[] deserialize(YamlElement element, Type typeOfT) {
        Type compt = ((Class) typeOfT).getComponentType();
        int num = CharMatcher.is('[').countIn(((Class) typeOfT).getName());
        TypeAdapter<T> adapter = fusionYAML.getTypeAdapter(typeOfT);
        LinkedList obj = new LinkedList<>();
        //Object o = adapter.deserialize(element, typeOfT);
        Type type = TypeToken.of(typeOfT).getRawType();
        element.getAsYamlArray().forEach(e -> ((LinkedList) obj).add(fusionYAML.deserialize(e, YamlUtils.toObject0(e).getClass())));
        System.out.println(obj);
        //if (!o.getClass().getName().equals("[" + ((Class) typeOfT).getName()))
          //  return null;
        return (T[]) obj.toArray();
    }

}