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
package com.github.fusionyaml.configurations;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.document.YamlComment;
import com.github.fusionyaml.exceptions.YamlException;
import com.github.fusionyaml.io.DocumentReader;
import com.github.fusionyaml.object.YamlObject;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;

/**
 * This class is a synchronized class that converts {@link File} data into {@link YamlObject} data.
 * You can then retrieve and update file data in this class. To save the {@link File}, use
 * {@link #save(File)} or {@link #save(DumperOptions, File)}
 */
public class FileConfiguration extends YamlConfiguration {

    /**
     * The file
     */
    private File file;

    /**
     * This constructor requires a {@link File} instance. The {@link File} contents will then
     * be copied into a {@link YamlObject}, which gives the user the ability to modify and
     * retrieve data.
     * <p>
     * To save the updated data, call {@link #save(DumperOptions, File)} or {@link #save(File)}
     *
     * @param file The file that will get its contents copied
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
            this.object = reader.toYamlObject(fusionYAML);
        }
        try (DocumentReader reader = new DocumentReader(file)) {
            YamlComment comment;
            while ((comment = reader.nextComment()) != null)
                comments.add(comment);
        }
    }

    public void save() throws IOException {
        this.save(file);
    }

    public File getFile() {
        return file;
    }

}
