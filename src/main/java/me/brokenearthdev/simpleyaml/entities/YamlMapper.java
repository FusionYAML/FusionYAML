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
