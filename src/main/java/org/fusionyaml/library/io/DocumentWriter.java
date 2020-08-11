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

    public DocumentWriter(Writer writer, int buff) {
        super(writer, buff);
    }

    public DocumentWriter(Writer writer) {
        super(writer);
    }

    public DocumentWriter(File file, int buff) {
        super(file, buff);
    }

    public DocumentWriter(File file) {
        super(file);
    }

    @Override
    public void write(@NotNull YamlElement element, FusionYAML fusionYAML) throws IOException {
        Object dumpable = Utilities.toDumpableObject(Utilities.removeNullIfEnabled(element, fusionYAML));
        Yaml yaml = new Yaml(Utilities.getDumperOptions(fusionYAML.getYamlOptions()));
        String dumped = yaml.dump(dumpable);
        this.write(dumped);
    }

    @Override
    public void write(@NotNull YamlElement element) throws IOException {
        this.write(element, new FusionYAML());
    }

}
