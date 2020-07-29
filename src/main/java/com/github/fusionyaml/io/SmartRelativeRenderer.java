package com.github.fusionyaml.io;

import com.github.fusionyaml.document.YamlComment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SmartRelativeRenderer extends CommentRenderer {

    @Override
    public Map.Entry<String, Integer> addComment(List<String> info, YamlComment comment, int maxWidth) {
        boolean commented = lineCommented(info, comment.getLineNumber());
        int iLn = comment.getLineNumber();
        int iCol = comment.getColumn();
        while (commented) {
            commented = lineCommented(info, iLn);
            iLn++;
        }
        String[] cmt = splitComment(comment.getText(), maxWidth);
        String ln = info.get(iLn);
        if (ln.replace(" ", "").equals("")) {
            StringBuilder lnBuilder = new StringBuilder();
            for (int i = 0; i < iCol; i++)
                lnBuilder.append(" ");
            ln = lnBuilder.toString();
        }
        if (iLn > comment.getLineNumber() || iCol > comment.getColumn())
            createSpace(info, iLn, iCol);
        StringBuilder builder = new StringBuilder();
        boolean start = true;
        for (String str : cmt) {
            if (start) start = false;
            else builder.append("\n");
            builder.append(str);
        }
        String str = comment.isInline() ? ln + " " + builder.toString() : (space(iCol) +
                builder.toString() + "\n" + ln);
        HashMap<String, Integer> map = new HashMap<>();
        map.put(str, iLn);
        return new LinkedList<>(map.entrySet()).get(0);
    }

    private String space(int space) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < space; i++)
            builder.append(" ");
        return builder.toString();
    }

}
