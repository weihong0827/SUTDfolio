package com.example.sutdfolio.data.model;

import java.util.List;

public class ReadPost extends Posts {
    private List<User> peopleInvolved;
    private int upvoteCount;
    private Course courseNo;
    private List<Tag> tag;
    private boolean liked;
    private boolean editable;

    public ReadPost(String title, String desc, List<Image> image, List<Tag> tag, List<User> peopleInvolved, Course courseNo, int term, String telegram, String linkIn, String youtube, Boolean publish, int upvoteCount, boolean liked, boolean editable) {
        super(title,desc, image,term, telegram, linkIn, youtube, publish);
        this.peopleInvolved = peopleInvolved;
        this.courseNo = courseNo;
        this.upvoteCount = upvoteCount;
        this.editable = editable;
        this.liked = liked;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ReadPost{" +
                "peopleInvolved=" + peopleInvolved +
                ", upvoteCount=" + upvoteCount +
                ", courseNo=" + courseNo +
                ", tags=" + tag +
                '}';
    }

    public List<Tag> getTag() {
        return tag;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isEditable() {
        return editable;
    }

    public List<User> getPeopleInvolved() {
        return peopleInvolved;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public Course getCourseNo() {
        return courseNo;
    }

    public List<Tag> getTags() { return tag; }
}