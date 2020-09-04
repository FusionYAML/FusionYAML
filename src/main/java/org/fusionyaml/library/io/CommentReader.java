package org.fusionyaml.library.io;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link CommentReader} reads the comments present in a
 * YAML document(s). Please note that this may be unpredictable
 * if there is any mention of the hashtag symbol ("#") in a YAML
 * scalar.
 */
public class CommentReader extends YamlReader {
    
    /**
     * @param reader A {@link Reader}
     */
    public CommentReader(Reader reader) {
        super(reader);
    }
    
    /**
     * Creates a {@link java.io.BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param reader A {@link Reader}
     * @param buff   The buffer
     */
    public CommentReader(Reader reader, int buff) {
        super(reader, buff);
    }
    
    /**
     * @param str A {@link String}, which will be read from
     */
    public CommentReader(String str) {
        super(str);
    }
    
    /**
     * Creates a {@link java.io.BufferedReader} with buffer equal to the value
     * passed in.
     *
     * @param file A {@link File}
     * @param buff The buffer
     */
    public CommentReader(File file, int buff) {
        super(file, buff);
    }
    
    /**
     * Creates a {@link java.io.BufferedReader} with buffer almost equal to the {@link File}'s
     * length depending on the size of the file.
     *
     * @param file A {@link File}
     */
    public CommentReader(File file) {
        super(file);
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
        while ((c = reader.read()) != -1) {
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
    
    /**
     * Reads all of the comment present.
     *
     * @param inOrder Whether the returned list should include comments
     *                in order ({@link LinkedList}) or not ({@link ArrayList})
     * @return A list of comments
     * @throws IOException If an exception occurred
     */
    public List<String> readComments(boolean inOrder) throws IOException {
        List<String> comments = (inOrder ? new LinkedList<>() : new ArrayList<>());
        String comment;
        while ((comment = nextComment()) != null)
            comments.add(comment);
        return comments;
    }
    
}
