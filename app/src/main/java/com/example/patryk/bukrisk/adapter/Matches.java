package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-05-03.
 */

public class Matches {

    String match;
    String A;
    String X;
    String B;
    String date;

    public String getMatch() {
        return match;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public String getX() {
        return X;
    }

    public String getDate() {
        return date;
    }

    public Matches(String match, String a, String x, String b, String date) {

        this.match = match;
        this.date=date;
        this.A = a;
        this.X = x;

        this.B = b;
    }
}
