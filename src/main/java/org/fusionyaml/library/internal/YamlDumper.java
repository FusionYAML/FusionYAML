package org.fusionyaml.library.internal;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.Writer;

/**
 * Dumps {@link YamlElement} through snakeyaml's dumper
 */
public final class YamlDumper {
    
    private final Converter converter = new Converter();
    
    /**
     * Dumps the element to a {@link String}
     *
     * @param element    The element
     * @param fusionYAML The {@link FusionYAML} object
     * @return A YAML {@link String}
     */
    public String dump(YamlElement element, FusionYAML fusionYAML) {
        DumperOptions options = converter.toDumperOptions(fusionYAML.getYamlOptions());
        return new Yaml(options).dump(converter.toSnakeYAML(element));
    }
    
    /**
     * Dumps the element to a {@link Writer}
     *
     * @param writer     The writer
     * @param element    The {@link YamlElement}
     * @param fusionYAML The {@link FusionYAML} object
     */
    public void dump(Writer writer, YamlElement element, FusionYAML fusionYAML) {
        DumperOptions options = converter.toDumperOptions(fusionYAML.getYamlOptions());
        new Yaml(options).dump(converter.toSnakeYAML(element), writer);
    }
    
}
