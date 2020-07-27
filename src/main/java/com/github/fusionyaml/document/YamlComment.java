package com.github.fusionyaml.document;

/**
 * An immutable class representing a yaml comment.
 */
public class YamlComment {

    /**
     * Whether (or not) the comment is inline
     */
    private final boolean inline;

    /**
     * The line number
     */
    private final int line;

    /**
     * The column (distance from margin)
     */
    private final int col;

    /**
     * The text
     */
    private final String text;


    public YamlComment(boolean inline, int line, int col, String text) {
        this.inline = inline;
        this.line = line;
        this.col = col;
        this.text = text;
    }

    /**
     * @return The line number
     */
    public int getLineNumber() {
        return line;
    }

    /**
     * @return The column (distance from margin)
     */
    public int getColumn() {
        return col;
    }

    /**
     * @return Whether (or not) the comment is inline
     */
    public boolean isInline() {
        return inline;
    }

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

}
