package com.terryyessfung.whatsins.Model;

public class User {
    private String id;
    private String avatar;
    private String username;
    private String bio;

    public User() {
    }

    public User(String id, String avatar, String username, String bio) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
