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
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.Writer;

/**
 * This {@link Listener} is called when a {@link Configuration} is saved to a {@link Writer}
 * through Configuration#save methods or {@link Configuration#save(DumperOptions, File)}.
 *
 * @deprecated The event listener system will be replaced with a better system
 */
@Deprecated
public interface FileSaveListener extends Listener {

    /**
     * This {@link Listener} is called when a {@link Configuration} is saved to a {@link Writer}
     * through Configuration#save methods or {@link Configuration#save(DumperOptions, File)}.
     *
     * @param configuration The {@link Configuration} that was saved to a {@link Writer}
     * @param writer        The {@link Writer}
     */
    void onSave(Configuration configuration, Writer writer);

}
