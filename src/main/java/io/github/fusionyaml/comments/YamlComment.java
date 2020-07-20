package io.github.fusionyaml.comments;


public class YamlComment implements Comment {

    private int distance;
    private String text;
    private CommentType type;
    private CommentPlacement placement;

    public YamlComment(String text, CommentType type, CommentPlacement placement, int distanceFromMargin) {
        this.text = text;
        this.type = type;
        this.placement = placement;
        this.distance = distanceFromMargin;
    }

    public YamlComment(String text, CommentType type, CommentPlacement placement) {
        this.text = text;
        this.type = type;
        this.placement = placement;
    }

    @Override
    public int getDistanceFromMargin() {
        return distance;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public CommentType getCommentType() {
        return type;
    }

    @Override
    public CommentPlacement getCommentPlacement() {
        return placement;
    }

}
