package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Allows the writing of one document at once. Unlike
 * {@link DocumentWriter}, this writer can support multi-document
 * YAML.
 * <p>
 * Each element written will be surrounded with document start and
 * end indicators
 */
public class MultiDocumentWriter extends YamlWriter {
    /**
     * Creates an instance of this class by copying the {@link BufferedWriter}
     * from the class passed in.
     *
     * @param writer The {@link YamlWriter}
     */
    protected MultiDocumentWriter(YamlWriter writer) {
        super(writer);
    }
    
    /**
     * Creates an instance of this class with buffer equal to the value
     * passed in.
     *
     * @param writer The writer
     * @param buff   The buffer
     */
    public MultiDocumentWriter(Writer writer, int buff) {
        super(writer, buff);
    }

    /**
     * Creates an instance of this class with buffer equal to 65536.
     *
     * @param writer The writer
     */
    public MultiDocumentWriter(Writer writer) {
        super(writer);
    }

    /**
     * Creates an instance of this class with buffer equal to the one passed
     * in. The {@link File} will be written to.
     *
     * @param file The {@link File}
     * @param buff The buffer
     */
    public MultiDocumentWriter(File file, int buff) {
        super(file, buff);
    }

    /**
     * Creates an instance of this class with buffer almost equal to the file's
     * length.
     *
     * @param file The {@link File}
     */
    public MultiDocumentWriter(File file) {
        super(file);
    }

    /**
     * Writes a document. The {@link YamlElement} passed in will be dumped and
     * treated as a whole document.
     *
     * @param element    The element
     * @param fusionYAML A {@link FusionYAML} instance
     * @throws IOException If an IO error occurred
     */
    @Override
    public void write(@NotNull YamlElement element, FusionYAML fusionYAML) throws IOException {
        DumperOptions options = Utilities.getDumperOptions(fusionYAML.getYamlOptions());
        options.setExplicitEnd(true);
        options.setExplicitStart(true);
        Yaml yaml = new Yaml(options);
        String str = yaml.dump(Utilities.toDumpableObject(Utilities.removeNullIfEnabled(element, fusionYAML)));
        buffedWriter.write(str);
    }

    /**
     * Writes a document. The {@link YamlElement} passed in will be dumped and
     * treated as a whole document. Each document will be surrounded with start
     * and end indicators.
     *
     * @param element The element
     * @throws IOException If an IO error occurred
     */
    @Override
    public void write(@NotNull YamlElement element) throws IOException {
        this.write(element, new FusionYAML());
    }

    /**
     * Writes multiple documents at once
     *
     * @param elements   The yaml elements, each representing a document
     * @param fusionYAML A {@link FusionYAML} instance
     * @throws IOException If an IO error occurred
     */
    public void writeDocuments(Iterable<YamlElement> elements, FusionYAML fusionYAML) throws IOException {
        for (YamlElement element : elements) {
            this.write(element, fusionYAML);
        }
    }

    /**
     * Writes multiple documents at once
     *
     * @param elements The yaml elements, each representing a document
     * @throws IOException If an IO error occurred
     */
    public void writeDocuments(Iterable<YamlElement> elements) throws IOException {
        FusionYAML fusionYAML = new FusionYAML();
        for (YamlElement element : elements) {
            this.write(element, fusionYAML);
        }
    }

}
