package com.github.fusionyaml.io;

import com.github.fusionyaml.document.YamlComment;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Renders comments.
 */
public abstract class CommentRenderer {

    public abstract Map.Entry<String, Integer> addComment(List<String> info, YamlComment comment, int maxWidth);

    public boolean lineCommented(List<String> info, int ln) {
        String line = info.get(ln);
        char before = '\0';
        boolean start = true;
        for (char character : line.toCharArray()) {
            if (character == '#' && (before == ' ' || start)) {
                return true;
            } else {
                before = character;
                start = false;
            }
        }
        return false;
    }

    public String[] splitComment(String text, int maxWidth) {

        List<String> list = Splitter.on(' ').omitEmptyStrings().splitToList(text.replace('\n', ' '));
        List<String> finalList = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            String x = list.get(i);
            if (x.length() < maxWidth && i + 1 != list.size()) {
                StringBuilder toAdd = new StringBuilder(x);
                for (int k = i + 1; k < list.size(); k++) {
                    String str = list.get(k);
                    // add next string if the sum is less than the
                    // maximum width
                    if (toAdd.length() + str.length() < maxWidth) {
                        toAdd.append(" ").append(str);
                        i++;
                    } else break;
                }
                finalList.add("#" + toAdd.toString());
            } else if (x.length() > maxWidth) {
                Splitter.fixedLength(maxWidth).splitToList(x).forEach(f -> finalList.add("#" + f));
            } else finalList.add("#" + x);
        }
        return finalList.toArray(new String[0]);
    }

    public static void createSpace(@NotNull List<String> info, int ln, int col) {
        while (ln >= info.size())
            info.add("");
        String line = info.get(ln);
        StringBuilder colBuilder = new StringBuilder(line);
        while (col >= colBuilder.length())
            colBuilder.append(" ");
        info.set(ln, colBuilder.toString());
    }

}
