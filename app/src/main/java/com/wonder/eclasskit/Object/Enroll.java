package com.wonder.eclasskit.Object;

public class Enroll {
    private String id;
    private String subject;
    private String syear;
    private String tname;


    public Enroll(String id, String tname, String subject, String syear) {
        this.id = id;
        this.tname = tname;
        this.subject = subject;
        this.syear = syear;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSyear() {
        return syear;
    }

    public void setSyear(String syear) {
        this.syear = syear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
