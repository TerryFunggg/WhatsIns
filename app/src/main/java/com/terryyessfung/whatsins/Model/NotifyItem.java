package com.terryyessfung.whatsins.Model;

public class NotifyItem {
    private String userid,message,postid;
    private boolean ispost;

    public NotifyItem() {
    }

    public NotifyItem(String userid, String message, String postid, boolean ispost) {
        this.userid = userid;
        this.message = message;
        this.postid = postid;
        this.ispost = ispost;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
