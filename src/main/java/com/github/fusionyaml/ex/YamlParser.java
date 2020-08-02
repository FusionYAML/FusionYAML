package com.github.fusionyaml.ex;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.exceptions.YamlParseFailedException;
import com.github.fusionyaml.object.YamlArray;
import com.github.fusionyaml.object.YamlDocument;
import com.github.fusionyaml.object.YamlElement;
import com.github.fusionyaml.object.YamlObject;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class YamlParser {

    private final Yaml yaml = new Yaml();
    private final FusionYAML fusionYAML;

    public YamlParser(FusionYAML yaml) {
        this.fusionYAML = yaml;
    }


    private String readToStr(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader, 65536)) {
            StringBuilder builder = new StringBuilder();
            String ln;
            boolean first = true;
            while ((ln = bufferedReader.readLine()) != null) {
                if (first) first = false;
                else builder.append("\n");
                builder.append(ln);
            }
            return builder.toString();
        } catch (IOException e) {
            throw new YamlParseFailedException(e);
        }
    }

    public YamlElement load(String yaml) {
        Object obj = this.yaml.load(yaml);
        if (obj instanceof Map)
            return $DataBridge.toYamlObject((Map) obj);
        else if (obj instanceof List)
            return $DataBridge.toYamlArray((List) obj);
        throw new UnsupportedOperationException();
    }

    public YamlObject loadAsMap(String yaml) {
        Map<String, Object> map = this.yaml.loadAs(yaml, Map.class);
        return $DataBridge.toYamlObject(map, fusionYAML);
    }

    public YamlArray loadAsList(String yaml) {
        List<Map<String, Object>> list = this.yaml.loadAs(yaml, List.class);
        List<YamlElement> lst = new LinkedList<>();
        list.forEach(m -> {
            if (m != null)
                lst.add($DataBridge.toYamlObject(m));
        });
        return new YamlArray(lst);
    }

    public List<YamlDocument> loadDocuments(String yaml) {
        List<YamlDocument> docs = new LinkedList<>();
        this.yaml.loadAll(yaml).forEach(o -> {
            if (o instanceof Map)
                docs.add(new YamlDocument($DataBridge.toYamlObject((Map<String, Object>) o, fusionYAML)));
            else if (o instanceof List)
                docs.add(new YamlDocument($DataBridge.toYamlArray((List<Object>) o)));
            else throw new UnsupportedOperationException();
        });
        return docs;
    }

}
