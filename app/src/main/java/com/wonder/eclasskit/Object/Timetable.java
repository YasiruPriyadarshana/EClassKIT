package com.wonder.eclasskit.Object;

public class Timetable {
    private String date;
    private String grade;
    private String time;
    private String institute;
    private String gcalss;

    public Timetable() {
    }
    public Timetable(String date, String grade, String time, String institute, String gcalss) {
        this.date = date;
        this.grade = grade;
        this.time = time;
        this.institute = institute;
        this.gcalss = gcalss;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getGcalss() {
        return gcalss;
    }

    public void setGcalss(String gcalss) {
        this.gcalss = gcalss;
    }
}
