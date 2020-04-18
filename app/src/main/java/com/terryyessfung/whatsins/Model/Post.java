package com.terryyessfung.whatsins.Model;

public class Post {
    private String postID;
    private String postImage;
    private String publisher;

    public Post() {
    }

    public Post(String postID, String postImage, String publisher) {
        this.postID = postID;
        this.postImage = postImage;
        this.publisher = publisher;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
