package org.fusionyaml.library.configurations;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.io.DocumentReader;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.object.YamlObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * A {@link Configuration} for {@link Reader}s. Upon initialization, a {@link YamlObject}
 * object is created. This configuration can be reloaded by calling {@link #reload(InputStream)}
 */
public class InputStreamConfiguration extends YamlConfiguration {
    
    private final InputStream stream;
    
    public InputStreamConfiguration(InputStream stream, FusionYAML yaml) throws IOException {
        super(yaml);
        this.reload(stream);
        this.stream = stream;
    }
    
    public InputStreamConfiguration(InputStream stream) throws IOException {
        this(stream, new FusionYAML());
    }

    /**
     * Reloads this configuration by re-reading the {@link InputStream}
     * passed in
     *
     * @param stream An {@link InputStream}
     * @throws IOException If an IO error occurred
     */
    public void reload(InputStream stream) throws IOException {
        try (DocumentReader reader = new DocumentReader(new InputStreamReader(stream))) {
            YamlElement element = reader.readDocument();
            if (element.isYamlNull())
                this.object = new YamlObject();
            else this.object = element.getAsYamlObject();
        }
    }
    
    public InputStream getStream() {
        return stream;
    }
    
}
