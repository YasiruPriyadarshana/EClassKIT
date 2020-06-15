package com.wonder.eclasskit.Object;

public class UploadPDF {
    private String name;
    private String url;



    public String getImgurl() {
        return imgurl;
    }

    public UploadPDF(String name, String url, String imgurl) {
        this.name = name;
        this.url = url;
        this.imgurl = imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    private String imgurl;

    public UploadPDF() {
    }

    public UploadPDF(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


