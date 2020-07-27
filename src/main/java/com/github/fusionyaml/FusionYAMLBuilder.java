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
package com.github.fusionyaml;

import com.github.fusionyaml.serialization.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link FusionYAML} builder where you can set some options that'll be immutable in a {@link FusionYAML}
 * object.
 *
 * @deprecated Use {@link FusionYAML.Builder} instead.
 */
@Deprecated
public class FusionYAMLBuilder {

    /**
     * The {@link Map} containing custom {@link TypeAdapter}s
     */
    private final Map<Type, TypeAdapter> classTypeAdapterMap = new HashMap<>();

    /**
     * The {@link DumperOptions}
     */
    private DumperOptions options = null;

    /**
     * The map containing {@link Class}es where its {@link TypeAdapter}s will be removed (only optional
     * {@link TypeAdapter}s will be removed)
     */
    private final List<Type> rem = new ArrayList<>();

    /**
     * Sets the {@link TypeAdapter} for the class. {@link FusionYAML} will use this {@link TypeAdapter} to
     * serialize and deserialize an {@link Object} of the same class.
     *
     * @param clazz The class
     * @param adapter The {@link TypeAdapter} for the class
     * @return This instance
     */
    public FusionYAMLBuilder setTypeAdapterFor(@NotNull Type clazz, TypeAdapter adapter) {
        if (adapter == null)
            return removeTypeAdapterFor(clazz);
        classTypeAdapterMap.put(clazz, adapter);
        rem.remove(clazz);
        return this;
    }

    /**
     * Sets the {@link TypeAdapter} for the class if the {@link TypeAdapter} for the {@link Class} doesn't exist.
     * {@link FusionYAML} will use this {@link TypeAdapter} to serialize and deserialize an {@link Object} of the
     * same class.
     *
     * @param clazz The class
     * @param adapter The {@link TypeAdapter} for the class
     * @return This instance
     */
    public FusionYAMLBuilder setTypeAdapterForIfNotExists(@NotNull Type clazz, TypeAdapter adapter) {
        if (adapter == null)
            return removeTypeAdapterFor(clazz);
        if (!classTypeAdapterMap.containsKey(clazz)) {
            classTypeAdapterMap.put(clazz, adapter);
            rem.remove(clazz);
        }
        return this;
    }

    /**
     * Removes the {@link TypeAdapter} for the class. This will only work on custom {@link TypeAdapter}s added
     * by the user and "optional" {@link TypeAdapter}s. {@link TypeAdapter}s for these types won't be removed:
     * <ul>
     *     <li>ObjectTypeAdapter</li>
     *     <li>MapTypeAdapter</li>
     *     <li>CollectionTypeAdapter</li>
     *     <li>PrimitiveTypeAdapter</li>
     * </ul>
     *
     * @param clazz The class
     * @return This instance
     */
    public FusionYAMLBuilder removeTypeAdapterFor(@NotNull Type clazz) {
        classTypeAdapterMap.remove(clazz);
        rem.add(clazz);
        return this;
    }

    /**
     * Sets the {@link DumperOptions} for the {@link FusionYAML} that is yet to be created
     *
     * @param options The {@link DumperOptions}
     * @return This instance
     */
    public FusionYAMLBuilder setDumperOptions(DumperOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Creates a {@link FusionYAML} instance. The {@link FusionYAML}'s settings will be set to the settings
     * set in this object.
     *
     * @return A {@link FusionYAML} instance
     */
    public FusionYAML build() {
        return new FusionYAML(null, classTypeAdapterMap);
    }

}