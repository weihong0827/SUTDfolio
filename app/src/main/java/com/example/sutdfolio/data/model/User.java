package com.example.sutdfolio.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

    private String _id;
    private String name;
    private String email;
    private int studentId;
    private String pillar;
    private String avatar;
    private String aboutMe;
    private int class_of;

    public User(String _id, String name, String email, int studentId, String pillar, String avatar, String aboutMe, int class_of) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.pillar = pillar;
        this.avatar = avatar;
        this.aboutMe = aboutMe;
        this.class_of = class_of;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getPillar() {
        return pillar;
    }

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getClass_of() {
        return class_of;
    }

    public void setClass_of(int class_of) {
        this.class_of = class_of;
    }


}