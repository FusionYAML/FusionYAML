package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * This class enables users to read
 */
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

    public YamlElement readDocument() {
        Yaml snakeYAML = new Yaml();
        Object read = snakeYAML.load(this);
        return Utilities.toElement(read);
    }

    public <T> T deserializeTo(Type type, FusionYAML yaml) {
        YamlElement element = readDocument();
        return yaml.deserialize(element, type);
    }


}
