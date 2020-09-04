package org.fusionyaml.library.io;

import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads a single element at a time.
 */
public class ElementReader extends YamlReader {
    
    private LinkedList<YamlElement> elements;
    private int index = 0;
    
    /**
     * @param reader A {@link Reader}
     */
    public ElementReader(Reader reader) {
        super(reader);
    }
    
    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    public ElementReader(Reader reader, int buff) {
        super(reader, buff);
    }
    
    /**
     * @param str A {@link String}, which will be read from
     */
    public ElementReader(String str) {
        super(str);
    }
    
    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param file A {@link File}
     * @param buff The buffer
     */
    public ElementReader(File file, int buff) {
        super(file, buff);
    }
    
    /**
     * Creates a {@link BufferedReader} with buffer almost equal to the {@link File}'s
     * length depending on the size of the file.
     *
     * @param file A {@link File}
     */
    public ElementReader(File file) {
        super(file);
    }
    
    /**
     * Reads the next {@link YamlElement} and returns it. The method
     * returns an element one by one in the document.
     * <p>
     * If there are two documents, then the method will also read the
     * first yaml element in the document after reading the last one in
     * the last document.
     *
     * @return The {@link YamlElement}
     */
    public YamlElement nextElement() {
        if (getAllElements().size() <= index) return null;
        YamlElement element = getAllElements().get(index);
        index++;
        return element;
    }
    
    /**
     * Gets all of the elements.
     *
     * @return A list of {@link YamlElement}s
     */
    public List<YamlElement> getAllElements() {
        if (elements == null) {
            elements = createList();
        }
        return elements;
    }
    
    private LinkedList<YamlElement> createList() {
        Iterable<Object> loaded = new Yaml().loadAll(buffReader);
        LinkedList<YamlElement> created = new LinkedList<>();
        loaded.forEach(e -> {
            YamlElement converted = Utilities.toElement(e);
            if (converted.isYamlObject()) {
                converted.getAsYamlObject().forEach((k, v) -> created.add(v));
            }
            if (converted.isYamlArray()) {
                converted.getAsYamlArray().forEach(created::add);
            }
        });
        return created;
    }
    
    
}
