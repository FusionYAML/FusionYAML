package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.internal.Converter;
import org.fusionyaml.library.object.YamlElement;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * This class enables users to read a single document. By invoking
 * any of its methods, all of the yaml data will be read, so you need
 * to make sure that you haven't previously used this object to read
 * anything.
 */
public class DocumentReader extends YamlReader {


    /**
     * @param reader A {@link Reader}
     */
    public DocumentReader(Reader reader) {
        super(reader);
    }

    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    public DocumentReader(Reader reader, int buff) {
        super(reader, buff);
    }

    /**
     * @param str A {@link String}, which will be read from
     */
    public DocumentReader(String str) {
        super(str);
    }

    /**
     * Creates a {@link java.io.BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param file A {@link File}
     * @param buff The buffer
     */
    public DocumentReader(File file, int buff) {
        super(file, buff);
    }

    /**
     * Creates a {@link java.io.BufferedReader} with buffer almost equal to the {@link File}'s
     * length depending on the size of the file.
     *
     * @param file A {@link File}
     */
    public DocumentReader(File file) {
        super(file);
    }
    
    /**
     * Reads and returns the document.
     *
     * @return A document
     */
    public YamlElement readDocument() {
        Yaml snakeYAML = new Yaml();
        Converter converter = new Converter();
        Object read = snakeYAML.load(buffReader);
        return converter.toElement(read);
    }

    /**
     * Deserializes the document into a class of type {@link T}.
     *
     * @param type The type
     * @param yaml A {@link FusionYAML} instance
     * @param <T>  The type
     * @return A class of type {@link T} deserialized from the
     * {@link YamlElement} returned in {@link #readDocument()}
     */
    public <T> T deserializeTo(Type type, FusionYAML yaml) {
        YamlElement element = readDocument();
        return yaml.deserialize(element, type);
    }

}
