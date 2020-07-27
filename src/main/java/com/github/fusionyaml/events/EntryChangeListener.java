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
package com.github.fusionyaml.events;

import com.github.fusionyaml.object.YamlObject;

import java.util.List;

/**
 * This {@link Listener} is called when data is modified in a {@link YamlObject} object. Setters
 * in {@link YamlObject} cause this to be called. Methods that remove path(s) also calls this
 * {@link Listener}.
 */
public interface EntryChangeListener extends Listener {

    /**
     * Called when data is modified in a {@link YamlObject} object. Setts in {@link YamlObject}
     * along with methods that remove path(s) cause this method to be called.
     *
     * @param object The {@link YamlObject} where the data is modified
     * @param path The path to the object
     * @param value The value the path contains
     */
    void onChange(YamlObject object, List<String> path, Object value);

}
