package com.example.sutdfolio.data.model;

public class Course {
    private final String courseDesc;
    private final String courseName;
    private final String courseNo;
    private final String _id;

    public Course(String courseName, String courseNo,String _id) {
        this.courseDesc = "";
        this.courseName = courseName;
        this.courseNo = courseNo;
        this._id = _id;
    }

    public Course(String courseDesc, String courseName, String courseNo,String _id) {
        this.courseDesc = courseDesc;
        this.courseName = courseName;
        this.courseNo = courseNo;
        this._id = _id;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseNo() {
        return courseNo;
    }


}
