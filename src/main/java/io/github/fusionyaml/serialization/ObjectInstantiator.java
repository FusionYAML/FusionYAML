package io.github.fusionyaml.serialization;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.utils.YamlUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Responsible for creating an {@link Object} and for collecting info about
 * {@link Field}s and converting them into a {@link Map} of {@link String}s and
 * {@link Object}.
 * <p>
 * This class is mainly used in {@link ObjectTypeAdapter} and is not intended for
 * public usage unless there is a need to retrieve information or modify
 */
public class ObjectInstantiator {

    /**
     * An instance of {@link FusionYAML}
     */
    private FusionYAML fusionYAML;

    public ObjectInstantiator(FusionYAML yaml) {
        this.fusionYAML = yaml;
    }

    public List<Field> getFieldsForSerialization(Type type) {
        List<Field> allFields = getFields(type);
        if (!doOnlyExposed(type)) {
            allFields.removeAll(getExcludedFields(type));
            allFields.forEach(f -> {
                Expose expose = getExpose(f);
                if ((expose != null && !expose.serialization()) || hasExcludedAnnotation(f)) allFields.remove(f);
            });
            return allFields;
        } else {
            List<Field> fields = new LinkedList<>();
            allFields.forEach(f -> {
                Expose e = getExpose(f);
                if ((e != null && e.serialization()) || hasExcludedAnnotation(f)) fields.add(f);
            });
            return fields;
        }
    }

    public List<Field> getFieldsForDeserialization(Type type) {
        List<Field> allFields = getFields(type);
        if (!doOnlyExposed(type)) {
            allFields.removeAll(getExcludedFields(type));
            allFields.forEach(f -> {
                Expose expose = getExpose(f);
                if ((expose != null && !expose.deserialization()) || hasExcludedAnnotation(f)) allFields.remove(f);
            });
            return allFields;
        } else {
            List<Field> fields = new LinkedList<>();
            allFields.forEach(f -> {
                Expose expose = getExpose(f);
                if ((expose != null && expose.deserialization()) || hasExcludedAnnotation(f)) fields.add(f);
            });
            return fields;
        }
    }

    public void assignFields(Object o, Map map, Type type) throws YamlDeserializationException {
        List<Field> fields = getFieldsForDeserialization(type);
        for (Object key : map.keySet()) {
            for (Field field : fields) {
                if (key.toString().equals(field.getName())) {
                    try {
                        field.setAccessible(true);
                        Object found = map.get(field.getName());
                        Object deserialized = getTypeAdapter(field).deserialize(YamlUtils.toElement(found), field.getType());
                        if (deserialized == null) continue;
                        //if (deserialized instanceof Map)
                            //deserialized = objDeserializer.deserialize(YamlUtils.toElement(deserialized), field.getType());
                        field.set(o, deserialized);
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new YamlDeserializationException(e);
                    }
                }
            }
        }
    }

    public Map<String, YamlElement> toSerializedMap(Object o, Type type) {
        List<Field> fields = getFieldsForSerialization(type);
        Map<String, YamlElement> map = new LinkedHashMap<>();
        try {
            for (Field field : fields) {
                boolean prev = field.isAccessible();
                field.setAccessible(true);
                map.put(getSerializedName(field), getTypeAdapter(field).serialize(field.get(o), field.getType()));
                field.setAccessible(prev);
            }
        } catch (Exception e) {
            throw new YamlSerializationException(e);
        }
        return map;
    }

    public List<Field> getFields(Type type) {
        Class<?> clazz = (Class<?>) type;
        Field[] fields = clazz.getDeclaredFields();
        return new LinkedList<>(Arrays.asList(fields));
    }

    public List<Field> getFieldsWithAnnotation(Type type, Class<? extends Annotation> annotation) {
        List<Field> fields = new LinkedList<>();
        for (Field field : ((Class<?>) type).getFields()) {
            if (field.getAnnotation(annotation) != null) fields.add(field);
        }
        return fields;
    }

    public List<Field> getExcludedFields(Type type) {
        return getFieldsWithAnnotation(type, Exclude.class);
    }

    public boolean doOnlyExposed(Type type) {
        return ((Class<?>) type).getAnnotation(OnlyExposed.class) != null;
    }

    public Expose getExpose(Field field) {
        return field.getAnnotation(Expose.class);
    }

    public boolean hasExcludedAnnotation(Field field) {
        return field.getAnnotation(Exclude.class) != null;
    }

    public String getSerializedName(Field field) {
        SerializedName name = field.getAnnotation(SerializedName.class);
        return name != null ? name.value() : field.getName();
    }

    public <T> TypeAdapter<T> getTypeAdapter(Field field) throws IllegalAccessException, InstantiationException {
        YamlAdapter adapter = field.getAnnotation(YamlAdapter.class);
        return adapter != null ? adapter.value().newInstance() : fusionYAML.getTypeAdapter(field.getType());
    }

}
