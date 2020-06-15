package com.wonder.eclasskit.Object;

public class Review {
    private int number;
    private double per;

    public Review() {
    }

    public Review(int number, double per) {
        this.number = number;
        this.per = per;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPer() {
        return per;
    }

    public void setPer(double per) {
        this.per = per;
    }
}
