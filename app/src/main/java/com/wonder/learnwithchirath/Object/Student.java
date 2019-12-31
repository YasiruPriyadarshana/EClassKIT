package com.wonder.learnwithchirath.Object;

public class Student {
    private String name;
    private String email;
    private String mobile_number;
    private String grade;

    public Student() {

    }

    public Student(String name, String email, String mobile_number, String grade) {
        this.name = name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.grade = grade;
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

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
