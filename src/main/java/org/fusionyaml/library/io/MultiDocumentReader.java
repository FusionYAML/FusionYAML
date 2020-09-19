package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlParseFailedException;
import org.fusionyaml.library.internal.Converter;
import org.fusionyaml.library.object.YamlElement;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * This class enables people to read different {@link YamlElement}s within
 * a document.
 */
public class MultiDocumentReader extends YamlReader {


    /**
     * @param reader A {@link Reader}
     */
    public MultiDocumentReader(Reader reader) {
        super(reader);
    }

    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    public MultiDocumentReader(Reader reader, int buff) {
        super(reader, buff);
    }

    /**
     * @param str A {@link String}, which will be read from
     */
    public MultiDocumentReader(String str) {
        super(str);
    }

    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param file A {@link File}
     * @param buff The buffer
     */
    public MultiDocumentReader(File file, int buff) {
        super(file, buff);
    }

    /**
     * Creates a {@link BufferedReader} with buffer almost equal to the {@link File}'s
     * length depending on the size of the file.
     *
     * @param file A {@link File}
     */
    public MultiDocumentReader(File file) {
        super(file);
    }

    /**
     * Reads all of the documents.
     *
     * @return A {@link List} of documents
     */
    public Iterable<YamlElement> readDocuments() {
        try {
            Converter converter = new Converter();
            Yaml yaml = new Yaml();
            Iterable<Object> iterable = yaml.loadAll(buffReader);
            List<YamlElement> elementLists = new LinkedList<>();
            iterable.forEach(o -> elementLists.add(converter.toElement(o)));
            return elementLists;
        } catch (Exception e) {
            throw new YamlParseFailedException(e);
        }
    }

    /**
     * Deserializes all documents into a object of type {@link T} of type
     * similar to the type passed in.
     *
     * @param type The type
     * @param yaml A {@link FusionYAML} object, which will be used for
     *             deserializing
     * @param <T>  The type
     * @return A deserialized {@link List} with objects of type {@link T}
     */
    public <T> Iterable<T> deserializeAllTo(Type type, FusionYAML yaml) {
        LinkedList<T> des = new LinkedList<>();
        this.readDocuments().forEach(doc -> des.add(yaml.deserialize(doc, type)));
        return des;
    }

}
