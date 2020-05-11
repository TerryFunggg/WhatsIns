package com.terryyessfung.whatsins.Model;

import java.util.List;

public class CommentList {
    List<Comment> comments;

    public CommentList() {
    }

    public CommentList(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
