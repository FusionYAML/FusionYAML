package com.github.fusionyaml.io;

import com.github.fusionyaml.document.YamlComment;
import com.github.fusionyaml.exceptions.YamlException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class YamlReader extends Reader {

    protected BufferedReader buffReader;
    protected Reader reader;
    protected int buff;
    protected Buffer charBuffer;

    protected int lines = 0;

    public YamlReader(Reader reader) {
        this(reader, 65536);
    }

    public YamlReader(Reader reader, int buff) {
        this.reader = reader;
        this.buff = buff;
        this.buffReader = new BufferedReader(reader, buff);
        this.charBuffer = CharBuffer.allocate(buff);
    }

    public YamlReader(String str) {
        this(new StringReader(str));
        buff = nearestBuff(str.length());
    }

    public YamlReader(File file, int buff) {
        if (!file.exists())
            throw new YamlException(new FileNotFoundException("File doesn't exist"));
        reader = createFR(file);
        buffReader = new BufferedReader(reader, buff);
        this.charBuffer = CharBuffer.allocate(buff);
    }

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

    public String readLine() throws IOException {
        lines++;
        return buffReader.readLine();
    }

    public YamlComment nextComment() throws IOException {
        StringBuilder builder = new StringBuilder();
        boolean start = false;
        int c;
        int dx = 0;
        boolean inline = false;
        while ((c = read()) != -1) {
            if (!start) dx++;
            if ((char) c == '\n' || (char) c == '\0') {
                if (start) break;
                dx = 0;
            }
            if (((char) c) == '#') {
                start = true;
                inline = dx != 0;
                continue;
            }
            if (start) {
                if ((char) c != '\n' && (char) c != '\0')
                    builder.append((char) c);
            }
        }
        return (start) ? new ReadComment(dx, lines, inline, builder.toString()) : null;
    }

    public Set<YamlComment> readComments(boolean resetFirst) throws IOException {
        if (resetFirst)
            super.reset();
        ArrayList<ReadComment> arrayList = new ArrayList<>();
        ReadComment current;
        while ((current = (ReadComment) nextComment()) != null) {
            arrayList.add(current);
        }
        return new HashSet<>(arrayList);
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

    protected String readToString() throws IOException {
        String ln;
        StringBuilder builder = new StringBuilder();
        boolean start = false;
        while ((ln = readLine()) != null) {
            if (!start) start = true;
            else builder.append("\n");
            builder.append(ln);
        }
        return builder.toString();
    }

    protected ArrayList<String> readToList() throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        char[] chars = readToString().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '\n' && i + 1 != chars.length)
                builder.append(chars[i]);
            else {

                arrayList.add(builder.toString());
                builder = new StringBuilder();
            }
        }
        ;
        return arrayList;
    }

    static class ReadComment extends YamlComment {

        public final int horizontalDist;
        public final int verticalDist;
        public final String text;

        public ReadComment(int horizontalDist, int verticalDist, boolean inline, String text) {
            super(inline, verticalDist, horizontalDist, text);
            this.horizontalDist = horizontalDist;
            this.verticalDist = verticalDist;
            this.text = text;
        }

        @Override
        public boolean equals(Object o2) {
            if (!(o2 instanceof ReadComment))
                return false;
            ReadComment c2 = (ReadComment) o2;
            return c2.horizontalDist == horizontalDist && c2.verticalDist == verticalDist &&
                    c2.text.equals(text);
        }

    }

}
