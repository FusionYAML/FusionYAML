package com.github.fusionyaml.io;

import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlNull;
import com.github.fusionyaml.object.YamlObject;
import com.github.fusionyaml.parser.DefaultParser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentReader extends YamlReader {

    public DocumentReader(Reader reader) {
        super(reader);
    }

    public DocumentReader(Reader reader, int buff) {
        super(reader, buff);
    }

    public DocumentReader(String str) {
        super(str);
    }

    public DocumentReader(File file, int buff) {
        super(file, buff);
    }

    public DocumentReader(File file) {
        super(file);
    }

    public YamlObject toYamlObject(FusionYAML yaml) throws IOException {
        Map<String, YamlElement> map = new LinkedHashMap<>();
        String str = readToString();
        DefaultParser parser = new DefaultParser(str);
        Map<String, Object> parsed = parser.map();
        parsed.forEach((key, value) -> {
            if (value != null) map.put(key, yaml.serialize(value, value.getClass()));
            else map.put(key, YamlNull.NULL);
        });
        return new YamlObject(map);
    }

    public <T> T deserializeTo(Type type, FusionYAML yaml, boolean resetFirst) throws IOException {
        if (resetFirst)
            this.reset();
        YamlObject object = toYamlObject(yaml);
        return yaml.deserialize(object, type);
    }

    public <T> T deserializeTo(Type type, FusionYAML yaml) throws IOException {
        return this.deserializeTo(type, yaml, true);
    }

}
