package com.terryyessfung.whatsins.Model;

import java.util.List;

public class User {
    private String _id;
    private String avatar;
    private String name;
    private int post_num;
    private int following_num;
    private int followers_num;
    private String[] followers;
    private String[] following;

    public User() {
    }

    public User(String _id, String avatar, String name, int post_num, int following_num, int followers_num, String[] followers, String[] following) {
        this._id = _id;
        this.avatar = avatar;
        this.name = name;
        this.post_num = post_num;
        this.following_num = following_num;
        this.followers_num = followers_num;
        this.followers = followers;
        this.following = following;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPost_num() {
        return post_num;
    }

    public void setPost_num(int post_num) {
        this.post_num = post_num;
    }

    public int getFollowing_num() {
        return following_num;
    }

    public void setFollowing_num(int following_num) {
        this.following_num = following_num;
    }

    public int getFollowers_num() {
        return followers_num;
    }

    public void setFollowers_num(int followers_num) {
        this.followers_num = followers_num;
    }

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }
}
