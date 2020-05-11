package com.terryyessfung.whatsins.Model;

import java.util.Date;

public class Comment {
    private String comment;
    private String publisher;
    private String createdAt;

    public Comment() {
    }

    public Comment(String comment, String publisher, String createdAt) {
        this.comment = comment;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
