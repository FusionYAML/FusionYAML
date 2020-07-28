package com.github.fusionyaml.io;

import com.github.fusionyaml.document.YamlComment;
import com.google.common.base.Splitter;

import java.util.LinkedList;
import java.util.List;

class CommentManager {

    static void createSpace(List<String> info, int ln, int col) {
        while (ln >= info.size())
            info.add("");
        String line = info.get(ln);
        StringBuilder colBuilder = new StringBuilder(line);
        while (col >= colBuilder.length())
            colBuilder.append(" ");
        info.set(ln, colBuilder.toString());
    }

    IntStringWrapper addComment(List<String> info, YamlComment comment, int maxWidth) {
        boolean commented = lineCommented(info, comment.getLineNumber());
        int iLn = comment.getLineNumber();
        int iCol = comment.getColumn();
        while (commented) {
            commented = lineCommented(info, iLn);
            iLn++;
        }
        String[] cmt = splitComment(comment.getText(), maxWidth);
        String ln = info.get(iLn);
        //if (iCol > ln.length())

        //while (!ln.substring(iCol).trim().equals(""))
        //  iCol++;
        if (iLn > comment.getLineNumber() || iCol > comment.getColumn())
            createSpace(info, iLn, iCol);
        StringBuilder builder = new StringBuilder();
        boolean start = true;
        for (String str : cmt) {
            if (start) start = false;
            else builder.append("\n");
            builder.append(str);
        }
        String str = comment.isInline() ? ln + " " + builder.toString() : (builder.toString() + "\n" + ln);
        IntStringWrapper s = new IntStringWrapper();
        s.str = str;
        s.num = iLn;
        return s;
    }

    boolean lineCommented(List<String> info, int ln) {
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

    String[] splitComment(String text, int maxWidth) {
        List<String> str = Splitter.on(' ').omitEmptyStrings().splitToList(text.replace('\n', ' '));
        List<String> finalList = concatUntil(str, maxWidth);
        return finalList.toArray(new String[0]);
    }
//
//
//    for (int i = 0; i < str.size(); i++) {
//
//        String string = str.get(i);
//        if (i + 1 != str.size()) {
//            String next = str.get(i + 1);
//            if (string.length() + next.length() <= maxWidth - 1) {
//                finalList.add("#" + string + " " + next);
//            } else finalList.add("#" + string);
//        }
//        else if (string.length() > maxWidth - 1) {
//            Splitter.fixedLength(maxWidth - 1).splitToList(string).forEach(f -> finalList.add("#" + f));
//        } else {
//            finalList.add("#" + string);
//        }
//    }

    private List<String> concatUntil(List<String> list, int maxWidth) {
        List<String> finalList = new LinkedList<>();
        int avoid = 0;
        for (int i = 0; i < list.size(); i++) {
            if (avoid != 0) {
                avoid--;
                continue;
            }
            String x = list.get(i);
            if (x.length() < maxWidth - 1 && i + 1 != list.size()) {
                StringBuilder toAdd = new StringBuilder(x);
                for (int k = i + 1; k < list.size(); k++) {
                    String str = list.get(k);
                    if (toAdd.length() + str.length() < maxWidth) {
                        toAdd.append(" ").append(str);
                        avoid++;
                    } else break;
                }
                finalList.add("#" + toAdd.toString());
            } else if (x.length() > maxWidth) {
                Splitter.fixedLength(maxWidth).splitToList(x).forEach(f -> finalList.add("#" + f));
            } else {
                finalList.add("#" + x);
            }
        }
        return finalList;
    }

    static class IntStringWrapper {
        int num;
        String str;
    }

}
