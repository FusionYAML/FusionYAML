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

import java.util.Map;

public abstract class Deserializer {

    public <T> T deserialize(Object serialized, Class<T> as) throws YamlDeserializationException {
        if (serialized instanceof Map)
            return deserialize((Map) serialized, as);
        if (as.isPrimitive() || serialized instanceof String) {
            if (!serialized.getClass().isPrimitive() && !(serialized instanceof String))
                throw new YamlDeserializationException("Cannot deserialize " + serialized.getClass().getName()
                        + ": class type is primitive or string and " + serialized.getClass().getName() + " is not");
            return (T) serialized;
        }
        return null;
    }

    public abstract <T> T deserialize(Map map, Class<T> as) throws YamlDeserializationException;

}
