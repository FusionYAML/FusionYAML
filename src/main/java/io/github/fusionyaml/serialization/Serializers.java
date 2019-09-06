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

/**
 * A package-private class that contains {@link Serializer} constants
 */
class Serializers {

    /**
     * {@link ObjectSerializer} instance
     */
    static final ObjectSerializer OBJECT_SERIALIZER = new ObjectSerializer();

    /**
     * {@link CollectionSerializer} instance
     */
    static final CollectionSerializer COLLECTION_SERIALIZER = new CollectionSerializer();

    /**
     * {@link MapSerializer} instance
     */
    static final MapSerializer MAP_SERIALIZER = new MapSerializer();

    /**
     * {@link PrimitiveSerializer} instance
     */
    static final PrimitiveSerializer PRIMITIVE_SERIALIZER = new PrimitiveSerializer();

}
