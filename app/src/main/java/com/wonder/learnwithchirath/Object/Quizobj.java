package com.wonder.learnwithchirath.Object;

public class Quizobj {
    private String question;
    private String uriimg;
    private String ans1;
    private String ans2;
    private String ans3;
    private String ans4;
    private String ans5;
    private String type;

    public Quizobj() {
    }

    public Quizobj(String question, String uriimg, String ans1, String ans2, String ans3, String ans4, String ans5, String type) {
        this.question = question;
        this.uriimg = uriimg;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.ans5 = ans5;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUriimg() {
        return uriimg;
    }

    public void setUriimg(String uriimg) {
        this.uriimg = uriimg;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public void setAns4(String ans4) {
        this.ans4 = ans4;
    }

    public String getAns5() {
        return ans5;
    }

    public void setAns5(String ans5) {
        this.ans5 = ans5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
