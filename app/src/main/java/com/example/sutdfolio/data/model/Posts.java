package com.example.sutdfolio.data.model;

import java.util.Date;
import java.util.List;

public class  Posts {
    private String _id;

    private String title;

    private String desc;
    private List<Image> image;
    private int term;
    private String telegram;
    private String linkIn;
    private String youtube;
    private Boolean publish;
    public Posts(String title, String desc, List<Image> image, int term, String telegram, String linkIn, String youtube, Boolean publish) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.term = term;
        this.telegram = telegram;
        this.linkIn = linkIn;
        this.youtube = youtube;
        this.publish = publish;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", image=" + image +
                ", term=" + term +
                ", telegram='" + telegram + '\'' +
                ", linkIn='" + linkIn + '\'' +
                ", youtube='" + youtube + '\'' +
                ", publish=" + publish +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }


    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getLinkIn() {
        return linkIn;
    }

    public void setLinkIn(String linkIn) {
        this.linkIn = linkIn;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

}
