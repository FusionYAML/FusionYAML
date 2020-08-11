package org.fusionyaml.library.io;

import org.fusionyaml.library.exceptions.YamlException;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * A {@link YamlReader} is a class that essentially reads and parses YAML.
 * This reader relies on a {@link BufferedReader} for reading text with
 * buffer ranging from 4096 to 504288 depending on the yaml size.
 * <p>
 * Depending on the time of yaml, different {@link YamlReader}s can be
 * utilized. For example, {@link DocumentReader} is used when reading
 * yaml within one document whereas {@link MultiDocumentReader} can
 * read multiple documents.
 * <p>
 * A {@link YamlReader} can also be used to read comments.
 */
public abstract class YamlReader extends Reader implements AutoCloseable {

    protected BufferedReader buffReader;
    protected Reader reader;
    protected int buff;

    protected int lines = 0;

    /**
     * @param reader A {@link Reader}
     */
    public YamlReader(Reader reader) {
        this(reader, 65536);
    }

    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    public YamlReader(Reader reader, int buff) {
        this.reader = reader;
        this.buff = buff;
        this.buffReader = new BufferedReader(reader, buff);
    }

    /**
     * @param str A {@link String}, which will be read from
     */
    public YamlReader(String str) {
        this(new StringReader(str));
        buff = nearestBuff(str.length());
    }

    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param file A {@link File}
     * @param buff The buffer
     */
    public YamlReader(File file, int buff) {
        if (!file.exists())
            throw new YamlException(new FileNotFoundException("File doesn't exist"));
        reader = createFR(file);
        buffReader = new BufferedReader(reader, buff);
    }

    /**
     * Creates a {@link BufferedReader} with buffer almost equal to the {@link File}'s
     * length depending on the size of the file.
     *
     * @param file A {@link File}
     */
    public YamlReader(File file) {
        this(file, nearestBuff(file.length()));
    }

    private static FileReader createFR(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new YamlException(e);
        }
    }

    private static int nearestBuff(long num) {
        if (num > 524288) return 524288;
        int buff = 4096;
        while (num > buff) {
            buff *= 2;
        }
        return Math.min(buff, 524288);
    }

    /**
     * Reads a single character.  This method will block until a character is
     * available, an I/O error occurs, or the end of the stream is reached.
     *
     * <p> Subclasses that intend to support efficient single-character input
     * should override this method.
     *
     * @return The character read, as an integer in the range 0 to 65535
     * (<tt>0x00-0xffff</tt>), or -1 if the end of the stream has
     * been reached
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        char[] chars = new char[1];
        if (read(chars, 0, 1) == -1)
            return -1;
        return chars[0];
    }

    /**
     * Reads characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start storing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     * stream has been reached
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        int s = buffReader.read(cbuf, off, len);
        for (char character : cbuf) {
            if (character == '\n' || character == '\0') lines++;
        }
        return s;
    }

    /**
     * Closes the stream and releases any system resources associated with
     * it.  Once the stream has been closed, further read(), ready(),
     * mark(), reset(), or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        buffReader.close();
    }

    @Override
    public void reset() throws IOException {
        buffReader.reset();
    }

    /**
     * Reads the line (has the same effect as {@link BufferedReader#readLine()}
     *
     * @return The text of the line
     * @throws IOException If an IO error occurred
     */
    protected String readLine() throws IOException {
        lines++;
        return buffReader.readLine();
    }

    /**
     * Reads the next comment. If there aren't any comment left, the method
     * will return {@code null}.
     * <p>
     * This message is constructed so that retrieving all comments in a
     * document can take place in a while loop.
     *
     * @return The comment, or null if there aren't any
     * @throws IOException If an IO error occurred
     */
    public String nextComment() throws IOException {
        StringBuilder builder = new StringBuilder();
        boolean start = false;
        boolean escape = false;
        int c;
        while ((c = read()) != -1) {
            if ((char) c == '\n' || (char) c == '\0') {
                if (start) break;
            }
            if ((char) c == '\\') escape = true;
            if (escape && (char) c != '\\') escape = false;
            if (!escape) {
                if (((char) c) == '#') {
                    start = true;
                    continue;
                }
                if (start) {
                    if ((char) c != '\n' && (char) c != '\0')
                        builder.append((char) c);
                }
            }
        }
        return builder.toString();
    }

}
