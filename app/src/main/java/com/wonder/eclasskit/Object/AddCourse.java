package com.wonder.eclasskit.Object;

public class AddCourse {
    private String subjectname;
    private String class_yr;

    public AddCourse() {
    }

    public AddCourse(String subjectname, String class_yr) {
        this.subjectname = subjectname;
        this.class_yr = class_yr;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getClass_yr() {
        return class_yr;
    }

    public void setClass_yr(String class_yr) {
        this.class_yr = class_yr;
    }
}
