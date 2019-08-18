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
package me.brokenearthdev.simpleyaml.entities;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class YamlMapper {

    private String text;

    public YamlMapper(String text) {
        this.text = text;
    }

    public YamlMapper(File file) throws IOException {
        this(FileUtils.readFileToString(file, Charset.defaultCharset()));
    }



    public Map<?, ?> map() {
        try {
            return new Yaml().loadAs(text, Map.class);
        } catch (Exception e) {
            throw new YAMLException("Invalid YAML", e.getCause());
        }
    }

}
