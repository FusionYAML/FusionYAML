package com.github.fusionyaml.io;

import com.github.fusionyaml.$DataBridge;
import com.github.fusionyaml.FusionYAML;
import com.github.fusionyaml.document.YamlComment;
import com.github.fusionyaml.exceptions.YamlException;
import com.github.fusionyaml.object.YamlObject;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public abstract class YamlWriter extends Writer implements AutoCloseable {

    protected BufferedWriter buffedWriter;
    protected Writer writer;
    protected int buff;
    protected int line;

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
        this.buffedWriter = new BufferedWriter(writer, buff);
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
        List<String> list = new LinkedList<>(Splitter.on("\n").splitToList(dumped));
        List<Integer> skip = new ArrayList<>();
        comments.forEach(c -> skip.add(c.getLineNumber()));
        int maxLn = skip.stream().mapToInt(v -> v).max().getAsInt();
        int maxCol = comments.stream().mapToInt(YamlComment::getColumn).max().getAsInt();
        CommentManager.createSpace(list, maxLn, maxCol);
        CommentManager manager = new CommentManager();
        for (YamlComment comment : comments) {
            CommentManager.IntStringWrapper cmt =
                    manager.addComment(list, comment, fusionYAML.getYamlOptions().getWidth());
            list.set(cmt.num, cmt.str);
        }
        writeList(list);
        flush();
    }

    protected void writeList(List<String> list) throws IOException {
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            if (!first) builder.append("\n");
            else first = false;
            builder.append(str);
        }
        write(builder.toString());
    }

    /**
     * Writes a string.
     *
     * @param str String to be written
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void write(@NotNull String str) throws IOException {
        buffedWriter.write(str);
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
