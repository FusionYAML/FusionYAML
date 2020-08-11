package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.object.YamlElement;
import org.fusionyaml.library.utils.Utilities;
import org.yaml.snakeyaml.Yaml;

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

    public MultiDocumentReader(Reader reader) {
        super(reader);
    }

    public MultiDocumentReader(Reader reader, int buff) {
        super(reader, buff);
    }

    public MultiDocumentReader(String str) {
        super(str);
    }

    public MultiDocumentReader(File file, int buff) {
        super(file, buff);
    }

    public MultiDocumentReader(File file) {
        super(file);
    }

    public Iterable<YamlElement> readDocuments() {
        Yaml yaml = new Yaml();
        Iterable<Object> iterable = yaml.loadAll(this);
        List<YamlElement> elementLists = new LinkedList<>();
        iterable.forEach(o -> elementLists.add(Utilities.toElement(o)));
        return elementLists;
    }

    public <T> Iterable<T> deserializeAllTo(Type type, FusionYAML yaml) {
        LinkedList<T> des = new LinkedList<>();
        this.readDocuments().forEach(doc -> des.add(yaml.deserialize(doc, type)));
        return des;
    }

}
