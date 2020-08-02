package com.github.fusionyaml.io;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.object.YamlObject;
import com.github.fusionyaml.parser.DefaultParser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
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
        String str = readToString();
        DefaultParser parser = new DefaultParser(str);
        Map<String, Object> parsed = parser.map();
        return $DataBridge.toYamlObject(parsed, yaml);
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
