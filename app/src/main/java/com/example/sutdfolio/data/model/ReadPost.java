package com.example.sutdfolio.data.model;

import java.util.List;

public class ReadPost extends Posts {
    private List<User> peopleInvolved;
    private int upvoteCount;
    private Course courseNo;
    public ReadPost(String title, String desc, List<Image> image, List<User> peopleInvolved, Course courseNo, int term, String telegram, String linkIn, String youtube, Boolean publish, int upvoteCount) {
        super(title,desc, image,term, telegram, linkIn, youtube, publish);
        this.peopleInvolved = peopleInvolved;
        this.courseNo = courseNo;
        this.upvoteCount = upvoteCount;
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
}
