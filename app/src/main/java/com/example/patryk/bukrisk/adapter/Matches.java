package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-05-03.
 */

public class Matches {

    String id;
    String match;
    String score;
    String A;
    String X;
    String B;
    String teamA;
    String teamB;
    String date;
    String logoA;
    String logoB;



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

    public String getId() {
        return id;
    }


    public Matches(String id, String match, String teamA,String logoA, String teamB,String logoB, String score, String a, String x, String b, String date) {

        this.id=id;
        this.match = match;
        this.teamA = teamA;
        this.teamB = teamB;
        this.score=score;
        this.date=date;
        this.logoA=logoA;
        this.logoB=logoB;
        this.A = a;
        this.X = x;
        this.B = b;
    }
}
