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

import io.github.fusionyaml.events.DeserializationListener;
import io.github.fusionyaml.events.SerializationListener;
import io.github.fusionyaml.object.YamlElement;

public abstract class TypeAdapter<T> implements Serializer<T>, Deserializer<T> {

    protected DeserializationListener deserializationListener;
    protected SerializationListener serializationListener;

    @Override
    public void setOnDeserialize(DeserializationListener listener) {
        this.deserializationListener = listener;
    }

    @Override
    public void setOnSerialize(SerializationListener listener) {
        this.serializationListener = listener;
    }

    protected void callDeserializationEvent(Object deserialized, YamlElement serialized) {
        if (deserializationListener != null)
            deserializationListener.onDeserialization(this, deserialized, serialized);
    }

    protected void callSerializationEvent(Object obj, YamlElement serialized) {
        if (serializationListener != null)
            serializationListener.onSerialization(this, obj, serialized);
    }

}
