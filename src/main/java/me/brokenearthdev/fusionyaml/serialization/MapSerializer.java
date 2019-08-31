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
package me.brokenearthdev.fusionyaml.serialization;

import me.brokenearthdev.fusionyaml.utils.StorageUtils;
import me.brokenearthdev.fusionyaml.utils.YamlUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapSerializer extends ObjectSerializer {

    @Override
    public Map<String, Object> serialize(Object o) throws IllegalAccessException {
        if (!(o instanceof Map))
            throw new IllegalArgumentException("The value passed in is not a map");
        Map<String, Object> serialized = new LinkedHashMap<>();
        Map map = (Map) o;
        for (Object key : map.keySet()) {
            serialized.put(key.toString(), new ObjectSerializer().serialize(map.get(key)));
        }
        return serialized;
    }

}
