package org.fusionyaml.library.internal;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Loads YAML through snakeyaml's loader
 */
public final class YamlLoader {
    
    private final Converter converter = new Converter();
    
    /**
     * Loads a {@link YamlElement} given a {@link Reader}
     *
     * @param reader     The {@link Reader}
     * @param fusionYAML The {@link FusionYAML} object
     * @return The loaded {@link YamlElement}
     */
    public YamlElement load(Reader reader, FusionYAML fusionYAML) {
        return converter.toElement(new Yaml(converter.toDumperOptions(fusionYAML.getYamlOptions()))
                .load(reader));
    }
    
    /**
     * Loads Yaml from the given string
     *
     * @param string The string
     * @return The {@link YamlElement}
     */
    public YamlElement load(String string) {
        return load(new StringReader(string));
    }
    
    /**
     * Loads a {@link YamlElement} given a {@link Reader}
     *
     * @param reader The {@link Reader}
     * @return The loaded {@link YamlElement}
     */
    public YamlElement load(Reader reader) {
        return load(reader, new FusionYAML());
    }
    
    /**
     * Loads multiple documents given a {@link Reader}
     *
     * @param reader     The reader
     * @param fusionYAML The {@link FusionYAML}
     * @return The loaded documents
     */
    public List<YamlElement> loadDocuments(Reader reader, FusionYAML fusionYAML) {
        Iterable<Object> objects = new Yaml(converter.toDumperOptions(fusionYAML.getYamlOptions()))
                .loadAll(reader);
        List<YamlElement> elements = new LinkedList<>();
        objects.forEach(o -> elements.add(converter.toElement(o)));
        return elements;
    }
    
    /**
     * Loads multiple documents given a {@link String}
     *
     * @param string The {@link String}
     * @return The loaded documents
     */
    public List<YamlElement> loadDocuments(String string) {
        return loadDocuments(new StringReader(string));
    }
    
    /**
     * Loads multiple documents given a {@link Reader}
     *
     * @param reader The reader
     * @return The loaded documents
     */
    public List<YamlElement> loadDocuments(Reader reader) {
        return loadDocuments(reader, new FusionYAML());
    }
    
    
}
