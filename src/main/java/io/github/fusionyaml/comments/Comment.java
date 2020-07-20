package io.github.fusionyaml.comments;

import java.util.List;

/**
 * A comment represents a one-line comment in YAML
 */
public interface Comment {

    int getDistanceFromMargin();
    String getText();
    CommentType getCommentType();
    CommentPlacement getCommentPlacement();

}
