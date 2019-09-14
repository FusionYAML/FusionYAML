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

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.exceptions.YamlDeserializationException;
import io.github.fusionyaml.exceptions.YamlSerializationException;
import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.object.YamlPrimitive;
import io.github.fusionyaml.utils.YamlUtils;
import org.jetbrains.annotations.NotNull;

public class PrimitiveTypeAdapter extends TypeAdapter<Object> {

    public PrimitiveTypeAdapter(@NotNull FusionYAML fusionYAML) {
        super(fusionYAML);
    }

    @Override
    public Object deserialize(@NotNull YamlElement serialized) {
        if (!serialized.isYamlPrimitive())
            throw new YamlDeserializationException(serialized + " is not primitive");
        Object val = serialized.getAsYamlPrimitive().getValue();
        callDeserializationEvent(val, serialized);
        return val;
    }

    @Override
    public YamlElement serialize(Object obj) {
        if (!YamlUtils.isPrimitive(obj))
            throw new YamlSerializationException(obj + " is not a primitive nor a string");
        return new YamlPrimitive(obj);
    }
}
