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
import io.github.fusionyaml.object.YamlList;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

public class CollectionTypeAdapter extends TypeAdapter<Collection<Object>> {


    @Override
    public Collection<Object> deserialize(@NotNull YamlElement serialized) {
        if (!serialized.isYamlList())
            throw new YamlDeserializationException(serialized + " is not a serialized collection");
        YamlList serializedList = serialized.getAsYamlList();
        Collection<YamlElement> yamlElementCollection = serializedList.getList();
        LinkedList<Object> deserialized = new LinkedList<>();
        yamlElementCollection.forEach(e -> deserialized.add(YamlUtils.toObject0(e)));
        callDeserializationEvent(deserialized, serialized);
        return deserialized;
    }

    @Override
    public YamlElement serialize(@NotNull Collection<Object> obj) {
        YamlList list = YamlUtils.toYamlList(obj);
        callSerializationEvent(obj, list);
        return list;
    }
}
