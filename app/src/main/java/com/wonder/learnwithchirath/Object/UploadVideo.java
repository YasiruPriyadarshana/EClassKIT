package com.wonder.learnwithchirath.Object;

public class UploadVideo {
    private String videourl;
    private String vName;


    public UploadVideo() {
    }

    public UploadVideo(String videourl, String vName) {
        this.videourl = videourl;
        this.vName = vName;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }
}
