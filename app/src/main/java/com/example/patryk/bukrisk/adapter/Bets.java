package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-05-17.
 */

public class Bets {

    String id_match;
    String id_coupon;
    String course;
    String type;
    String risk;

    public Bets(String id_match, String id_coupon, String type, String course, String risk) {
        this.id_match = id_match;
        this.id_coupon = id_coupon;
        this.type = type;
        this.risk = risk;
        this.course = course;
    }


}
