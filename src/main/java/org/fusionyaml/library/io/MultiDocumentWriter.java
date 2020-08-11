package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.YamlOptions;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Allows the writing of one document at once. Unlike
 * {@link DocumentWriter}, this writer can support multi-document
 * YAML.
 * <p>
 * Each element written will be surrounded with document start and
 * end indicators regardless of the option set in {@link YamlOptions#isAllowDocStartAndEnd()}.
 * This is done to prevent any document clashes.
 */
public class MultiDocumentWriter extends YamlWriter {

    public MultiDocumentWriter(Writer writer, int buff) {
        super(writer, buff);
    }

    public MultiDocumentWriter(Writer writer) {
        super(writer);
    }

    public MultiDocumentWriter(File file, int buff) {
        super(file, buff);
    }

    public MultiDocumentWriter(File file) {
        super(file);
    }

    @Override
    public void write(@NotNull YamlElement element, FusionYAML fusionYAML) throws IOException {
        DumperOptions options = Utilities.getDumperOptions(fusionYAML.getYamlOptions());
        boolean before = fusionYAML.getYamlOptions().isAllowDocStartAndEnd();
        options.setExplicitEnd(true);
        options.setExplicitStart(true);
        Yaml yaml = new Yaml(options);
        String str = yaml.dump(Utilities.toDumpableObject(element));
        options.setExplicitEnd(before);
        options.setExplicitEnd(before);
        this.write(str);
    }

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
