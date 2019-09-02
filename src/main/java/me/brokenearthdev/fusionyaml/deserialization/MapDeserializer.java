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

import java.util.LinkedHashMap;
import java.util.Map;

public class MapDeserializer extends ObjectDeserializer {

    @Override
    public <T> T deserializeObject(Map map, Class<T> clazz) {
        throw new UnsupportedOperationException("Not " + ObjectSerializer.class.getName() +": " + this.getClass().getName());
    }

    @Override
    public Map deserialize(Object serializedObj) throws YamlDeserializationException {
        if (!(serializedObj instanceof Map))
            throw new YamlDeserializationException("The value passed in is not a serialized map");
        Map map = (Map) serializedObj;
        Map newMap = new LinkedHashMap();
        for (Object key : map.keySet())
            newMap.put(key, Deserializers.OBJECT_DESERIALIZER.deserialize(map.get(key)));
        return newMap;
    }
}
