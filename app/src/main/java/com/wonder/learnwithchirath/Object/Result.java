package com.wonder.learnwithchirath.Object;

public class Result {
    private String name;
    private String marks;
    private String img;

    public Result() {
    }

    public Result(String name, String marks, String img) {
        this.name = name;
        this.marks = marks;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
