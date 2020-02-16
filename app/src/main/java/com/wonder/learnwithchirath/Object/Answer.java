package com.wonder.learnwithchirath.Object;

import java.util.ArrayList;

public class Answer {
    private ArrayList<Integer> answer;
    private String name;

    public Answer(ArrayList<Integer> answer, String name) {
        this.answer = answer;
        this.name = name;
    }

    public Answer() {
    }

    public ArrayList<Integer> getCompletelist() {
        return answer;
    }

    public void setCompletelist(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
