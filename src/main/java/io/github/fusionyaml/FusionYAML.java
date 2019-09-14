/*
Copyright 2019 BrokenEarthDev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package io.github.fusionyaml;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.parser.DefaultParser;
import io.github.fusionyaml.parser.YamlParser;
import io.github.fusionyaml.serialization.*;
import io.github.fusionyaml.utils.YamlUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class FusionYAML {

    private static final Gson GSON_DEFAULT = new GsonBuilder().create();
    private static final Gson GSON_PRETTY_PRINTING = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, Object>>(){}.getType();
    private static final String DOCUMENT_BEGIN = "---\n";
    private static final String DOCUMENT_END = "...\n";
    private static final YamlParser DEFAULT_PARSER = new DefaultParser(DOCUMENT_BEGIN + DOCUMENT_END);
    private static final YamlParser.YamlType DEFAULT_YAML_TYPE = YamlParser.YamlType.MAP;
    private static final DumperOptions YAML_DEFAULT_DUMPER_OPTIONS = defaultDumperOptions();
    private static final Yaml YAML = new Yaml(YAML_DEFAULT_DUMPER_OPTIONS);
    private static final String EMPTY_JSON = "{}";
    private static final String EMPTY_YAML = "{}";

    public final Map<Class, TypeAdapter> classTypeAdapterMap = new HashMap<>();
    private final ObjectTypeAdapter typeAdapter = new ObjectTypeAdapter(this);

    public FusionYAML() {
       classTypeAdapterMap.put(Collection.class, new CollectionTypeAdapter(this));
       classTypeAdapterMap.put(Map.class, new MapTypeAdapter(this));
       classTypeAdapterMap.put(Number.class, new PrimitiveTypeAdapter(this));
       classTypeAdapterMap.put(Boolean.class, new PrimitiveTypeAdapter(this));
       classTypeAdapterMap.put(String.class, new PrimitiveTypeAdapter(this));
       classTypeAdapterMap.put(Character.class, new PrimitiveTypeAdapter(this));
    }

    public Map<Class, TypeAdapter> getTypeAdapterMap() {
        return classTypeAdapterMap;
    }

   private static DumperOptions defaultDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
   }

   public YamlObject fromYAML(String raw) {
        DEFAULT_PARSER.reload(raw);
        Map<String, YamlElement> mem = YamlUtils.toMap(DEFAULT_PARSER.map());
        return new YamlObject(mem, DEFAULT_PARSER.getYamlType());
   }

   public YamlObject fromJSON(String raw, YamlParser.YamlType type) {
        Map<String, Object> contents = GSON_DEFAULT.fromJson(raw, MAP_TYPE);
        return load(contents, type);
    }

    public YamlObject fromJSON(String raw) {
        return fromJSON(raw, DEFAULT_YAML_TYPE);
    }

    public YamlObject fromJSON(JsonObject object, YamlParser.YamlType type) {
        Map<String, Object> map = GSON_DEFAULT.fromJson(object, MAP_TYPE);
        Map<String, YamlElement> converted = YamlUtils.toMap(map);
        return new YamlObject(converted, type);
    }

    public YamlObject fromJSON(JsonObject object) {
        return fromJSON(object, DEFAULT_YAML_TYPE);
    }

   public YamlObject load(Map<String, Object> map, YamlParser.YamlType type) {
        Map<String, YamlElement> mem = YamlUtils.toMap(map);
        return new YamlObject(mem, type);
   }

   public YamlObject load(Map<String, Object> map) {
        return this.load(map, DEFAULT_YAML_TYPE);
   }


   public String toJSON(YamlObject object, Gson gson) {
        Map<String, Object> converted = YamlUtils.toMap0(object);
        return gson.toJson(converted);
    }

   public String toJSON(YamlObject object, boolean pretty) {
        final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(object, gson);
   }

   public String toJSON(YamlObject object) {
        return toJSON(object, false);
   }

   public String toJSON(String yaml, Gson gson) {
        DEFAULT_PARSER.reload(yaml);
        Map<String, Object> map = DEFAULT_PARSER.map();
        return gson.toJson(map);
   }

   public String toJSON(String yaml, boolean pretty) {
       final Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
       return toJSON(yaml, gson);
   }

   public String toJSON(String yaml) {
       return toJSON(yaml, false);
   }

   public String toJSON(Object o, Gson gson) {
        Map<String, Object> map = toMap(classTypeAdapterMap, typeAdapter, o);
        return gson.toJson(map);
   }

   private static Map<String, Object> toMap(Map<Class, TypeAdapter> classTypeAdapterMap, ObjectTypeAdapter typeAdapter, Object o) {
       Class<?> clazz = o.getClass();
       TypeAdapter adapter = null;
       for (Map.Entry entry : classTypeAdapterMap.entrySet())
           if (entry.getKey().equals(clazz))
               adapter = (TypeAdapter<?>) entry.getValue();
       YamlElement serialized;
       if (adapter != null)
           serialized = adapter.serialize(o);
       else serialized = typeAdapter.serialize(o);
       if (!serialized.isYamlObject()) {
           Map<String, YamlElement> map = new LinkedHashMap<>();
           map.put(o.toString(), serialized);
           serialized = new YamlObject(map);
       }
       return YamlUtils.toMap0(serialized.getAsYamlObject());
   }

   public String toJSON(Object o, boolean pretty) {
        Gson gson = (pretty) ? GSON_PRETTY_PRINTING : GSON_DEFAULT;
        return toJSON(o, gson);
   }

   public String toJSON(Object o) {
        return toJSON(o, false);
   }

   public String toYAML(Object o) {
        return YAML.dump(toMap(classTypeAdapterMap, typeAdapter, o));
   }

   public YamlElement toYAMLElement(Object o) {
        YamlElement element = YamlUtils.toElement(o, true);
        if (element != null)
            return element;
        Map<String, Object> map = toMap(classTypeAdapterMap, typeAdapter, o);
        DEFAULT_PARSER.reload(map);
        Map<String, YamlElement> converted = YamlUtils.toMap(map);
        return new YamlObject(converted);
   }

}
