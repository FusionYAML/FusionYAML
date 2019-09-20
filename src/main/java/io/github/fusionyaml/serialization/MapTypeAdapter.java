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
package io.github.fusionyaml.serialization;

import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlObject;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@link Map} TypeAdapter
 */
public class MapTypeAdapter extends TypeAdapter<Map<Object, Object>> {

    @Override
    public Map<Object, Object> deserialize(@NotNull YamlElement serialized) {
        if (!serialized.isYamlObject() && !serialized.isYamlNode())
            throw new YamlDeserializationException(serialized + " is not a serialized map");
        YamlObject object = (serialized.isYamlNode()) ? new YamlObject(serialized.getAsYamlNode().getChildren()) :
                serialized.getAsYamlObject();
        Map<Object, Object> map = new LinkedHashMap<>();
        object.getMap().forEach((k, v) -> map.put(k, YamlUtils.toObject0(v)));
        callDeserializationEvent(map, serialized);
        return map;
    }

    @Override
    public YamlElement serialize(@NotNull Map<Object, Object> obj) {
        Map<String, YamlElement> serializedMap = new LinkedHashMap<>();
        obj.forEach((k, v) -> serializedMap.put(k.toString(), YamlUtils.toElement(v, false)));
        YamlObject object = new YamlObject(serializedMap);
        callSerializationEvent(obj, object);
        return object;
    }
}
