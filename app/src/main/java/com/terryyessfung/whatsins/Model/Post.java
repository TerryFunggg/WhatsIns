package com.terryyessfung.whatsins.Model;

public class Post {
    private String _id;
    private String publisher;
    private String desc;
    private String image;
    private String label;
    private String[] like;

    public Post() {
    }

    public Post(String _id, String publisher, String desc, String image, String[] like,String label) {
        this._id = _id;
        this.publisher = publisher;
        this.desc = desc;
        this.image = image;
        this.like = like;
        this.label = label;
    }

    public String[] getLike() {
        return like;
    }

    public void setLike(String[] like) {
        this.like = like;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
