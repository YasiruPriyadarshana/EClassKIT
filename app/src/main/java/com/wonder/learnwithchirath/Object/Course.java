package com.wonder.learnwithchirath.Object;

public class Course {
    private String cName;
    private String cTeacher;
    private String uid;

    public Course() {
    }


    public Course(String cName, String cTeacher, String uid) {
        this.cName = cName;
        this.cTeacher = cTeacher;
        this.uid = uid;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcTeacher() {
        return cTeacher;
    }

    public void setcTeacher(String cTeacher) {
        this.cTeacher = cTeacher;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
