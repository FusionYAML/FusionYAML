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
package io.github.fusionyaml.events;

import io.github.fusionyaml.object.YamlElement;
import io.github.fusionyaml.serialization.Serializer;

/**
 * This {@link Listener} is called when an {@link Object} is serialized in the built-in
 * serializers. You can also call {@code this} {@link Listener} in your custom
 * serializers.
 */
public interface SerializationListener extends Listener {

    /**
     * Called when an {@link Object} is serialized in the built-in serializers. You can also
     * call {@code this} {@link Listener} in your custom serializers.
     *
     * @param serializer The {@link Serializer} that called this method
     * @param object The {@link Object} that was serialized
     * @param serializedObj The serialized object
     */
    void onSerialization(Serializer serializer, Object object, YamlElement serializedObj);

}
