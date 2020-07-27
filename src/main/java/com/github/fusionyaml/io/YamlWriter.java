package com.github.fusionyaml.io;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.document.YamlComment;
import com.github.fusionyaml.exceptions.YamlException;
import com.github.fusionyaml.object.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public abstract class YamlWriter extends Writer {

    private BufferedWriter buffedWriter;
    private Writer writer;
    private int buff;
    private int line;

    public YamlWriter(Writer writer, int buff) {
        this.writer = writer;
        this.buff = buff;
        this.buffedWriter = new BufferedWriter(writer, buff);
    }

    public YamlWriter(Writer writer) {
        this(writer, 65536);
    }

    public YamlWriter(File file, int buff) {
        if (!file.exists())
            throw new YamlException(new FileNotFoundException("File doesn't exist"));
        this.writer = createFW(file);
        this.buff = buff;
        this.buffedWriter = new BufferedWriter(writer);
    }

    public YamlWriter(File file) {
        this(file, nearestBuff(file.length()));
    }

    private static int nearestBuff(long num) {
        if (num > 524288) return 524288;
        int buff = 4096;
        while (num > buff) {
            buff *= 2;
        }
        return Math.min(buff, 524288);
    }

    private static Writer createFW(File file) {
        try {
            return new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(@NotNull YamlObject obj, FusionYAML fusionYAML, Set<YamlComment> comments)
            throws IOException {
        Map<String, Object> dumpable = $DataBridge.toDumpableMap(obj.getMap());
        Yaml yaml = new Yaml($DataBridge.getDumperOptions(fusionYAML.getYamlOptions()));
        String dumped = yaml.dump(dumpable);
        if (comments.size() == 0) {
            this.write(dumped);
            return;
        }
        YamlReader reader = new YamlReader(dumped) {
        };
        List<String> list = reader.readToList();
        List<Integer> skip = new LinkedList<>();
        comments.forEach(c -> skip.add(c.getLineNumber()));
        for (YamlComment comment : comments) {
            CommentPlacementManager.createSpace(list, comment.getLineNumber(), comment.getColumn());
            skip.removeAll(Collections.singletonList(comment.getLineNumber()));
            CommentPlacementManager manager = new CommentPlacementManager(comment.getText(), comment.isInline(),
                    comment.getColumn(), comment.getLineNumber(), list, new LinkedList<>(skip));
            skip.add(comment.getLineNumber());
            String cmt = manager.addComment(fusionYAML.getYamlOptions().getWidth());
            list.set(manager.ln, cmt);
        }
        writeList(list);
        flush();
    }

    private void writeList(List<String> list) throws IOException {
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            if (!first) builder.append("\n");
            else first = false;
            builder.append(str);
        }
        write(builder.toString());
    }

    private Map<String, Integer> addComment(int line, int horizontalDist, FusionYAML yaml,
                                            boolean seperateOverWidth, String text, List<String> data) {

        LinkedList<String> dataNew = new LinkedList<>(data);

        String lineText;
        String aft;
        String prev;

        if (line - 1 >= 0 && line - 1 < dataNew.size()) lineText = dataNew.get(line - 1);
        else lineText = "";
        if (line >= 0 && line < dataNew.size()) aft = dataNew.get(line);
        else aft = "";
        if (line - 2 >= 0 && line - 2 < dataNew.size()) prev = dataNew.get(line - 2);
        else prev = "";

        int width = lineText.length() + text.length();
        int disty = (seperateOverWidth && width > yaml.getYamlOptions().getWidth()) ?
                (line - 1 < 0) ? line + 1 : line - 1 : line;
        int distx = disty != line ? 0 : horizontalDist;

        String lineT = (line == disty) ? lineText : (line - 2 == disty) ? prev : aft;
        String after = (lineT.length() > horizontalDist) ? lineT.substring(horizontalDist) : "";
        String before = (lineT.length() > horizontalDist) ? lineT.substring(0, horizontalDist) : lineT;
        distx += after.length() - 1;
        while (disty >= dataNew.size()) dataNew.add("");
        while (lineT.length() + 1 > distx) distx++;

        StringBuilder ntBuilder = new StringBuilder();
        int index = Math.max(disty - 1, 0);
        StringBuilder cb = new StringBuilder(dataNew.get(index));

        while (cb.length() <= distx) cb.append(" ");
        char[] chars = cb.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            ntBuilder.append(chars[i]);
            if (i + 1 == distx)
                ntBuilder.append("#").append(text);
        }

        dataNew.set(index, ntBuilder.toString());
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put(ntBuilder.toString(), disty - 1);
        return map;
    }

    /**
     * Writes a string.
     *
     * @param str String to be written
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void write(@NotNull String str) throws IOException {
        super.write(str);
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void write(@NotNull char[] cbuf, int off, int len) throws IOException {
        buffedWriter.write(cbuf, off, len);
        for (char chars : cbuf) {
            if (chars == '\n' || chars == '\0') line++;
        }
    }

    /**
     * Flushes the stream.  If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination.  Then, if that destination is another character or
     * byte stream, flush it.  Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams.
     *
     * <p> If the intended destination of this stream is an abstraction provided
     * by the underlying operating system, for example a file, then flushing the
     * stream guarantees only that bytes previously written to the stream are
     * passed to the operating system for writing; it does not guarantee that
     * they are actually written to a physical device such as a disk drive.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        buffedWriter.flush();
    }

    /**
     * Closes the stream, flushing it first. Once the stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown. Closing a previously closed stream has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        buffedWriter.close();
    }
}
