package com.wonder.eclasskit.Object;

public class Teachers {
    private String name;
    private String email;
    private String desc;
    private String facebook;
    private String twitter;
    private String youtube;
    private String web;


    public Teachers() {
    }

    public Teachers(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Teachers(String name, String email, String desc, String facebook, String twitter, String youtube, String web) {
        this.name = name;
        this.email = email;
        this.desc = desc;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.web = web;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
