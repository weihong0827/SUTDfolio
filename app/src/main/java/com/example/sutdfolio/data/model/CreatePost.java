package com.example.sutdfolio.data.model;

import java.util.ArrayList;
import java.util.List;

public class CreatePost extends Posts {
    private List<Integer> peopleInvolved;
    private ArrayList<String> tags;
    private String courseNo;
    public CreatePost(String title, ArrayList<String> tags, String desc, List<Image> image, List<Integer> peopleInvolved, String courseNo, int term, String telegram, String linkIn, String youtube, Boolean publish) {
        super(title,desc, image,term, telegram, linkIn, youtube, publish);
        this.peopleInvolved = peopleInvolved;
        this.courseNo = courseNo;
        this.tags = tags;
    }

    public List<Integer> getPeopleInvolved() {
        return peopleInvolved;
    }

    public void setPeopleInvolved(List<Integer> peopleInvolved) {
        this.peopleInvolved = peopleInvolved;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }
}