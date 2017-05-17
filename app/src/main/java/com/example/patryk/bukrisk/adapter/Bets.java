package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-05-17.
 */

public class Bets {

    String id_match;
    String name;
    String id_coupon;
    String course;
    String type;
    String risk;

    public Bets(String id_match, String name, String id_coupon, String type, String course, String risk) {
        this.id_match = id_match;
        this.id_coupon = id_coupon;
        this.type = type;
        this.risk = risk;
        this.course = course;
        this.name = name;
    }

    public String getId_match() {
        return id_match;
    }

    public String getType() {
        return type;
    }

    public String getCourse() {
        return course;
    }

    public String getId_coupon() {
        return id_coupon;
    }

    public String getRisk() {
        return risk;
    }
}
