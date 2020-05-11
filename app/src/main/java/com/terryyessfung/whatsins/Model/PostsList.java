package com.terryyessfung.whatsins.Model;

import java.util.List;

public class PostsList {
    List<Post> posts;

    public PostsList() {
    }

    public PostsList(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
