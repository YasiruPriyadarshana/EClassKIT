package com.wonder.learnwithchirath.Object;

import java.util.ArrayList;

public class Answer {
    private ArrayList<Integer> answer;
    private String result;
    private String name;

    public Answer(ArrayList<Integer> answer, String result, String name) {
        this.answer = answer;
        this.result = result;
        this.name = name;
    }

    public Answer() {
    }

    public ArrayList<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
