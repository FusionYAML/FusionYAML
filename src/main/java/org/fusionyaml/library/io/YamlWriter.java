package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlException;
import org.fusionyaml.library.object.YamlElement;
import org.jetbrains.annotations.NotNull;

import java.io.*;

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

    public abstract void write(@NotNull YamlElement element, FusionYAML fusionYAML)
            throws IOException;

    public abstract void write(@NotNull YamlElement element) throws IOException;

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
