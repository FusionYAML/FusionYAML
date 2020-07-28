//package com.github.fusionyaml.io;
//
//import com.github.fusionyaml.$DataBridge;
//import com.github.fusionyaml.FusionYAML;
//import com.github.fusionyaml.document.Document;
//import com.github.fusionyaml.document.YamlComment;
//import com.github.fusionyaml.object.YamlObject;
//import com.google.common.base.Splitter;
//import org.jetbrains.annotations.NotNull;
//import org.yaml.snakeyaml.DumperOptions;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.*;
//
//public class DocumentWriter extends YamlWriter {
//
//    public DocumentWriter(Writer writer, int buff) {
//        super(writer, buff);
//    }
//
//    public DocumentWriter(Writer writer) {
//        super(writer);
//    }
//
//    public DocumentWriter(File file, int buff) {
//        super(file, buff);
//    }
//
//    public DocumentWriter(File file) {
//        super(file);
//    }
//
//    @Override
//    public void write(@NotNull YamlObject obj, FusionYAML fusionYAML, Set<YamlComment> comments) throws IOException {
//        Map<String, Object> dumpable = $DataBridge.toDumpableMap(obj.getMap());
//        Yaml yaml = new Yaml($DataBridge.getDumperOptions(fusionYAML.getYamlOptions()));;
//        String dumped = yaml.dump(dumpable);
//        if (comments.size() == 0) {
//            //if (fusionYAML.getYamlOptions().isAllowDocStartAndEnd())
//              //  dumped = new StringBuilder().append("---").append("\n").append(dumped)
//            this.write(dumped);
//            return;
//        }
//        List<String> list = new LinkedList<>(Splitter.on("\n").splitToList(dumped));
//        List<Integer> skip = new ArrayList<>();
//        comments.forEach(c -> skip.add(c.getLineNumber()));
//        int maxLn = skip.stream().mapToInt(v -> v).max().getAsInt();
//        int maxCol = comments.stream().mapToInt(YamlComment::getColumn).max().getAsInt();
//        CommentManager.createSpace(list, maxLn,  maxCol);
//        CommentManager manager = new CommentManager();
//        for (YamlComment comment : comments) {
//            CommentManager.IntStringWrapper cmt =
//                    manager.addComment(list, comment, fusionYAML.getYamlOptions().getWidth());
//            list.set(cmt.num, cmt.str);
//        }
//        writeList(list);
//        flush();
//    }
//
//    public void write(YamlObject object, FusionYAML yaml) throws IOException {
//        this.write(object, yaml, new HashSet<>());
//    }
//
//    public void write(Document document) throws IOException {
//        this.write(document.toYamlObject(), document.getFusionYAML(), new HashSet<>(document.getComments()));
//    }
//}
