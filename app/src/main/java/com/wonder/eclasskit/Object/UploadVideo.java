package com.wonder.eclasskit.Object;

public class UploadVideo {
    private String videourl;
    private String vName;
    private String thumbnailUrl;


    public UploadVideo() {
    }

    public UploadVideo(String videourl, String vName) {
        this.videourl = videourl;
        this.vName = vName;
    }

    public UploadVideo(String videourl, String vName, String thumbnailUrl) {
        this.videourl = videourl;
        this.vName = vName;
        this.thumbnailUrl = thumbnailUrl;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
