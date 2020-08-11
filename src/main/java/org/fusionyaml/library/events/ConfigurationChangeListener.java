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
package org.fusionyaml.library.events;

import org.fusionyaml.library.configurations.Configuration;

import java.util.List;

/**
 * This {@link Listener} is called when a {@link Configuration}'s data is changed by adding, removing,
 * or modifying data. Call {@link Configuration#setOnConfigChange(ConfigurationChangeListener)} to
 * register this listener in a {@link Configuration}
 * <p>
 * This {@link Listener} usually gets called when one of the setters in {@link Configuration} is
 * invoked. Methods that remove path also will cause {@code this} {@link Listener} to be called.
 */
public interface ConfigurationChangeListener extends Listener {

    /**
     * Called when a {@link Configuration}'s data is changed by adding, removing, or modifying
     * data.
     * <p>
     * This {@link Listener} usually gets called when one of the setters in {@link Configuration} is
     * invoked. Methods that remove path also will cause {@code this} {@link Listener} to be called.
     *
     * @param configuration The {@link Configuration} where the event occurred
     * @param path The path to the value. The value in the path may be changed, added,
     *             or removed.
     * @param value Th value set in the {@code path}
     */
    void onChange(Configuration configuration, List<String> path, Object value);

}
