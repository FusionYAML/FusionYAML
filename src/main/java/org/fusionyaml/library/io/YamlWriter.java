package org.fusionyaml.library.io;

import org.fusionyaml.library.FusionYAML;
import org.fusionyaml.library.exceptions.YamlException;
import org.fusionyaml.library.object.YamlElement;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * A {@link YamlWriter} is a class responsible for writing {@link YamlElement}s.
 * This class relies on the {@link BufferedWriter} with buffer between 4096 (common)
 * to 504288 (rarely) depending on the yaml size.
 * <p>
 * Depending on the instance of the {@link YamlReader}, it may treat every
 * element as something within a document ({@link DocumentWriter}) or
 * as something representative of a document ({@link MultiDocumentWriter})
 */
public abstract class YamlWriter extends Writer implements AutoCloseable {

    protected BufferedWriter buffedWriter;
    protected Writer writer;
    protected int buff;
    protected int line;

    /**
     * Creates an instance of this class with buffer equal to the value
     * passed in.
     *
     * @param writer The writer
     * @param buff   The buffer
     */
    public YamlWriter(Writer writer, int buff) {
        this.writer = writer;
        this.buff = buff;
        this.buffedWriter = new BufferedWriter(writer, buff);
    }

    /**
     * Creates an instance of this class with buffer equal to 65536.
     *
     * @param writer The writer
     */
    public YamlWriter(Writer writer) {
        this(writer, 65536);
    }

    /**
     * Creates an instance of this class with buffer equal to the one passed
     * in. The {@link File} will be written to.
     *
     * @param file The {@link File}
     * @param buff The buffer
     */
    public YamlWriter(File file, int buff) {
        if (!file.exists())
            throw new YamlException(new FileNotFoundException("File doesn't exist"));
        this.writer = createFW(file);
        this.buff = buff;
        this.buffedWriter = new BufferedWriter(writer, buff);
    }

    /**
     * Creates an instance of this class with buffer almost equal to the file's
     * length.
     *
     * @param file The {@link File}
     */
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

    /**
     * Writes a {@link YamlElement}. Depending on the type of {@link YamlWriter} it
     * may be treated as a part of a document ({@link DocumentWriter}) or as a whole
     * document ({@link MultiDocumentWriter})
     *
     * @param element    The element
     * @param fusionYAML A {@link FusionYAML} instance
     * @throws IOException If an IO error occurred
     */
    public abstract void write(@NotNull YamlElement element, FusionYAML fusionYAML)
            throws IOException;

    /**
     * Writes a {@link YamlElement}. Depending on the type of {@link YamlWriter}. It
     * may be treated as a part of a document ({@link DocumentWriter}) or as a whole
     * document ({@link MultiDocumentWriter}).
     * <p>
     * The only difference between this and {@link #write(YamlElement, FusionYAML)} is
     * that this method doesn't require a {@link FusionYAML} object, so an empty
     * {@link FusionYAML} instance will be used to call {@link #write(YamlElement, FusionYAML)}
     *
     * @param element The element
     * @throws IOException If an IO error occurred
     * @see #write(YamlElement, FusionYAML)
     */
    public abstract void write(@NotNull YamlElement element) throws IOException;

}
