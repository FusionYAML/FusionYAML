package com.github.fusionyaml.io;

import com.google.common.base.Splitter;

import java.util.LinkedList;
import java.util.List;

public class CommentPlacementManager {

    String text;
    int col;
    int ln;
    boolean inline;
    List<String> info;
    List<Integer> skip;

    public CommentPlacementManager(String text, boolean inline, int col, int ln, List<String> info,
                                   List<Integer> skip) {
        this.text = text;
        this.col = col;
        this.ln = ln;
        this.info = info;
        this.skip = skip;
        this.inline = inline;
    }

    static void createSpace(List<String> info, int ln, int col) {
        while (ln >= info.size())
            info.add("");
        String line = info.get(ln);
        StringBuilder colBuilder = new StringBuilder(line);
        while (col >= colBuilder.length())
            colBuilder.append(" ");
        info.set(ln, colBuilder.toString());
    }

    boolean check(int ln, int col) {
        if (ln > info.size()) return true;
        String str = info.get(ln);
        if (str.length() > col) return true;
        return !str.trim().substring(col).equals("");
    }

    public String addComment(int maxWidth) {
        boolean commented = lineCommented();
        int iLn = ln;
        int iCol = col;
        while (commented) {
            commented = lineCommented();
            this.ln++;
        }
        String[] cmt = splitComment(maxWidth);
        String ln = info.get(this.ln);
        while (!ln.substring(col).trim().equals(""))
            col++;
        if (this.ln > iLn || this.col > iCol)
            createSpace(info, this.ln, col);
        StringBuilder builder = new StringBuilder();
        boolean start = true;
        for (String str : cmt) {
            if (start) start = false;
            else builder.append("\n");
            builder.append(str);
        }
        return inline ? ln + " " + builder.toString() : (builder.toString() + "\n" + ln);
    }

    public boolean lineCommented() {
        if (skip.contains(this.ln)) return false;
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

    public boolean inline() {
        if (ln >= info.size() - 1) return true;
        String txt = info.get(ln);
        if (text.length() <= col + 1) return true;
        String before = txt.substring(0, col + 1);
        return before.trim().length() > 0;
    }

    public String[] splitComment(int maxWidth) {
        List<String> str = Splitter.on(' ').omitEmptyStrings().splitToList(text);
        List<String> finalList = new LinkedList<>();
        for (int i = 0; i < str.size(); i++) {
            String string = str.get(i);
            if (string.length() > maxWidth) {
                Splitter.fixedLength(maxWidth).splitToList(string).forEach(s -> finalList.add("#" + s));
            } else finalList.add("#" + string);
        }
        return finalList.toArray(new String[0]);
    }


}
