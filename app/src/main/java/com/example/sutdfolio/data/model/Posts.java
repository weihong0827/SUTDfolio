package com.example.sutdfolio.data.model;

import java.util.Date;
import java.util.List;

public class Posts {
    private String title;
    private String desc;
    private List<Image> image;
    private List<User>peopleInvolved;
    private Course courseNo;
    private int term;
    private String telegram;
    private String linkIn;
    private String youtube;
    private Boolean publish;
    private int upvoteCount;

    public Posts(String title, String desc, List<Image> image, List<User> peopleInvolved, Course courseNo, int term, String telegram, String linkIn, String youtube, Boolean publish, int upvoteCount) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.peopleInvolved = peopleInvolved;
        this.courseNo = courseNo;
        this.term = term;
        this.telegram = telegram;
        this.linkIn = linkIn;
        this.youtube = youtube;
        this.publish = publish;
        this.upvoteCount = upvoteCount;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", image=" + image +
                ", peopleInvolved=" + peopleInvolved +
                ", courseNo=" + courseNo +
                ", term=" + term +
                ", telegram='" + telegram + '\'' +
                ", linkIn='" + linkIn + '\'' +
                ", youtube='" + youtube + '\'' +
                ", publish=" + publish +
                ", upvoteCount=" + upvoteCount +
                '}';
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

    public List<User> getPeopleInvolved() {
        return peopleInvolved;
    }

    public void setPeopleInvolved(List<User> peopleInvolved) {
        this.peopleInvolved = peopleInvolved;
    }

    public Course getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(Course courseNo) {
        this.courseNo = courseNo;
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

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvote) {
        this.upvoteCount = upvote;
    }
}
