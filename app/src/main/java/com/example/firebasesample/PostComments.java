package com.example.firebasesample;

public class PostComments {

    private String parentPostId;
    private String comment;
    private String commentAuthor;

    public PostComments() { }

    public PostComments(String postId, String comment, String author) {
        this.parentPostId = postId;
        this.comment = comment;
        this.commentAuthor = author;
    }

    public String getParentPostId() {
        return parentPostId;
    }

    public void setParentPostId(String parentPostId) {
        this.parentPostId = parentPostId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }
}
