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
package me.brokenearthdev.fusionyaml.deserialization;

import me.brokenearthdev.fusionyaml.serialization.ObjectSerializer;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.util.Map;

public class PrimitiveDeserializer implements Deserializer {

    @Override
    public <T> T deserializeObject(Map map, Class<T> clazz) {
        throw new UnsupportedOperationException("Not " + ObjectSerializer.class.getName() +": " + this.getClass().getName());
    }

    @Override
    public Object deserialize(Object serializedObj) throws YamlDeserializationException {
        if (!YamlUtils.isPrimitive(serializedObj))
            throw new YamlDeserializationException("The object passed in is not a primitive nor a " + String.class.getName());
        return serializedObj;
    }
}
