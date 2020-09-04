package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * This class enables users to write elements into a document. This class
 * treats each element as a value within a document, and not a representative
 * of a whole document.
 * <p>
 * For example, a {@link org.fusionyaml.library.object.YamlObject}
 * can be added using its write methods and will be merged with other
 * {@link org.fusionyaml.library.object.YamlObject}s
 */
public class DocumentWriter extends YamlWriter {
    
    /**
     * Creates an instance of this class with buffer equal to the value
     * passed in.
     *
     * @param writer The writer
     * @param buff   The buffer
     */
    public DocumentWriter(Writer writer, int buff) {
        super(writer, buff);
    }

    /**
     * Creates an instance of this class with buffer equal to 65536.
     *
     * @param writer The writer
     */
    public DocumentWriter(Writer writer) {
        super(writer);
    }

    /**
     * Creates an instance of this class with buffer equal to the one passed
     * in. The {@link File} will be written to.
     *
     * @param file The {@link File}
     * @param buff The buffer
     */
    public DocumentWriter(File file, int buff) {
        super(file, buff);
    }
    
    /**
     * Creates an instance of this class with buffer almost equal to the file's
     * length.
     *
     * @param file The {@link File}
     */
    public DocumentWriter(File file) {
        super(file);
    }
    
    /**
     * Creates an instance of this class by copying the {@link java.io.BufferedWriter}
     * from the class passed in.
     *
     * @param writer The {@link YamlWriter}
     */
    protected DocumentWriter(YamlWriter writer) {
        super(writer);
    }
    
    /**
     * Writes a {@link YamlElement} into a document. The {@link FusionYAML} object
     * passed in will be used for the dumper options.
     *
     * @param element    The element
     * @param fusionYAML A {@link FusionYAML} instance
     * @throws IOException If an IO error occurred
     */
    @Override
    public void write(@NotNull YamlElement element, FusionYAML fusionYAML) throws IOException {
        Yaml yaml = new Yaml(Utilities.getDumperOptions(fusionYAML.getYamlOptions()));
        Object converted = Utilities.toDumpableObject(Utilities.removeNullIfEnabled(element, fusionYAML));
        String dumped = yaml.dump(converted);
        buffedWriter.write(dumped);
    }
    
    /**
     * Writes a {@link YamlElement} into a document.
     *
     * @param element The element
     * @throws IOException If an IO error occurred
     */
    @Override
    public void write(@NotNull YamlElement element) throws IOException {
        this.write(element, new FusionYAML());
    }
    
    
    /**
     * Closes the stream, flushing it first. Once the stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown. Closing a previously closed stream has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        super.close();
    }
}
