package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-06-15.
 */

public class Coupons {

    String id_coupons;
    String id_user;
    String name;
    String money;
    String date;
    String total_course;
    String risk;
    String to_win;
    String paid_off;

    public Coupons(String id_coupons, String to_win, String total_course, String id_user, String name, String money, String date, String risk, String paid_off) {
        this.id_coupons = id_coupons;
        this.to_win = to_win;
        this.total_course = total_course;
        this.id_user = id_user;
        this.name = name;
        this.money = money;
        this.date = date;
        this.risk = risk;
        this.paid_off = paid_off;
    }

    public String getId_coupons() {
        return id_coupons;
    }

    public String getId_user() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public String getTotal_course() {
        return total_course;
    }

    public String getRisk() {
        return risk;
    }

    public String getTo_win() {
        return to_win;
    }

    public String getPaid_off() {
        return paid_off;
    }
}
