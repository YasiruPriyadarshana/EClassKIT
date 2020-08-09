package com.wonder.eclasskit.Object;

public class QuizHm {
    String name;
    String password;
    String time;

    public QuizHm() {
    }

    public QuizHm(String name, String password, String time) {
        this.name = name;
        this.password = password;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
