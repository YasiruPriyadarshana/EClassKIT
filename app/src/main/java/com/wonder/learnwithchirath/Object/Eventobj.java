package com.wonder.learnwithchirath.Object;

public class Eventobj {
    private String title;
    private String decription;
    private String time;
    private String uri;

    public Eventobj() {
    }

    public Eventobj(String title, String decription, String time, String uri) {
        this.title = title;
        this.decription = decription;
        this.time = time;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
