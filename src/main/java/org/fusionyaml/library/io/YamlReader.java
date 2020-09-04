package org.fusionyaml.library.io;

import org.fusionyaml.library.exceptions.YamlException;

import java.io.*;

/**
 * A {@link YamlReader} is a class that essentially reads and parses YAML.
 * This reader relies on a {@link BufferedReader} for reading text with
 * buffer ranging from 4096 to 504288 depending on the yaml size.
 * <p>
 * Depending on the type of yaml, different {@link YamlReader}s can be
 * utilized. For example, {@link DocumentReader} is used when reading
 * yaml within one document whereas {@link MultiDocumentReader} can
 * read multiple documents. {@link ElementReader} can be used to read a
 * single element at a time.
 * <p>
 * A {@link YamlReader} can also be used to read comments.
 */
public class YamlReader implements AutoCloseable {
    
    protected BufferedReader buffReader;
    protected Reader reader;
    protected int buff;
    
    private YamlReader() {
    }
    
    /**
     * @param reader A {@link Reader}
     */
    protected YamlReader(Reader reader) {
        this(reader, 65536);
    }
    
    /**
     * Creates a {@link BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    protected YamlReader(Reader reader, int buff) {
        this.reader = reader;
        this.buff = buff;
        this.buffReader = new BufferedReader(reader, buff);
    }
    
    /**
     * @param str A {@link String}, which will be read from
     */
    protected YamlReader(String str) {
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
    protected YamlReader(File file, int buff) {
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
    protected YamlReader(File file) {
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
    
    
}
