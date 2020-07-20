package io.github.fusionyaml.parser.special;

import io.github.fusionyaml.FusionYAML;
import io.github.fusionyaml.comments.Comment;
import io.github.fusionyaml.comments.CommentPlacement;
import io.github.fusionyaml.comments.CommentType;
import io.github.fusionyaml.parser.DefaultParser;
import io.github.fusionyaml.parser.Parser;
import io.github.fusionyaml.parser.YamlParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class CommentParser implements Parser {

    private String yaml;

    public CommentParser(String yaml) {
        this.yaml = yaml;
    }

    public Map<String, Integer> getCommentsTextLineNo() throws IOException {
        Map<String, Integer> text = new LinkedHashMap<>();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(yaml.getBytes())));
        int lineNo = 0;
        while ((line = reader.readLine()) != null) {
            lineNo++;
            StringBuilder commentBuilder = new StringBuilder();
            char[] array = line.toCharArray();
            boolean c = false;
            for (char character : array) {
                if (character == '#') c = true;
                if (c) commentBuilder.append(character);
            }
            text.put(commentBuilder.toString(), lineNo);
        }
        return text;
    }

    public CommentType findType(String commentText, Map<String, Integer> ctl, String yaml) {
        int index = yaml.indexOf(commentText);
        Integer line = ctl.get(commentText);
        if (index == -1 || line == null) return null;
        Set<Map.Entry<String, Integer>> set = ctl.entrySet();
        // check header
        return null;
    }


//
//    public List<Comment> getComments(String yaml) throws IOException {
//        List<String> texts = new LinkedList<>();
//        List<CommentType> types = new LinkedList<>();
//        List<CommentPlacement> placements = new LinkedList<>();
//        List<Integer> distances = new LinkedList<>();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(yaml.getBytes())));
//        String line;
//        int lineNo = 0;
//        while ((line = reader.readLine()) != null) {
//            lineNo ++;
//            char[] chars = line.toCharArray();
//            StringBuilder current = new StringBuilder();
//            boolean comment = false;
//            int margin = 0;
//            for (int i = 0; i < chars.length; i++) {
//                if (chars[i] == '#') {
//                    comment = true;
//                    margin = i;
//                }
//                if (comment) current.append(chars[i]);
//            }
//        }
//    }

}
