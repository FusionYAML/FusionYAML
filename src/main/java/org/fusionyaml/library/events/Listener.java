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

/**
 * A {@link Listener} is an interface that gets called when a certain event took place. For example,
 * {@link FileSaveListener} is called when a {@link Configuration}
 * is saved into a {@link java.io.File}
 * <p>
 * The listener should extend this interface and provide one method. That method will be called
 * if a certain event happened
 *
 * @deprecated The event listener system will be replaced with a better system
 */
@Deprecated
public interface Listener {
}
