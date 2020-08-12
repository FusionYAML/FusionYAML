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
package org.fusionyaml.library.configurations;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlException;
import org.fusionyaml.library.io.DocumentReader;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;

/**
 * This class is a synchronized class that converts {@link File} data into {@link YamlObject} data.
 * You can then retrieve and update file data in this class. To save the file, you can simple call
 * {@link #save()}
 */
public class FileConfiguration extends YamlConfiguration {

    /**
     * The file
     */
    private final File file;

    /**
     * This constructor requires a {@link File} instance. The {@link File} contents will then
     * be copied into a {@link YamlObject}, which gives the user the ability to modify and
     * retrieve data.
     * <p>
     * To save the updated data, call {@link #save(DumperOptions, File)} or {@link #save(File)}
     *
     * @param file The file that will get its contents copied
     * @param yaml a {@link FusionYAML} object
     * @throws IOException   If an IO error occurred
     * @throws YamlException If the parser map returns null
     */
    public FileConfiguration(File file, FusionYAML yaml) throws IOException, YamlException {
        super(yaml);
        this.file = file;
        this.reload();
    }

    public FileConfiguration(File file) throws IOException, YamlException {
        this(file, new FusionYAML());
    }

    public void reload() throws IOException {
        try (DocumentReader reader = new DocumentReader(file)) {
            YamlElement element = reader.readDocument();
            if (element.isYamlNull())
                object = new YamlObject();
            else object = element.getAsYamlObject();
        }
    }

    public void save() throws IOException {
        this.save(file);
    }

    public File getFile() {
        return file;
    }

}
