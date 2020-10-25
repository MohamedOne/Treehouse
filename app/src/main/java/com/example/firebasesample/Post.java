package com.example.firebasesample;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {

    private String postId;
    private String postTitle;
    private String postBody;
    private String postAuthor;
    private String postCreated;

    public Post() { }

    public Post(String mPostId, String mPostTitle, String mPostBody, String mPostAuthor, String mPostCreated) {
        this.postId = mPostId;
        this.postTitle = mPostTitle;
        this.postBody = mPostBody;
        this.postAuthor = mPostAuthor;
        this.postCreated = mPostCreated;
    }

    public String getPostId() { return postId; }

    public void setPostId(String mPostId) { this.postId = mPostId; }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String mPostTitle) {
        this.postTitle = mPostTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String mPostBody) {
        this.postBody = mPostBody;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String mPostAuthor) {
        this.postAuthor = mPostAuthor;
    }

    public String getPostCreated() {
        return postCreated;
    }

    public void setPostCreated(String mPostCreated) {
        this.postCreated = mPostCreated;
    }
}
