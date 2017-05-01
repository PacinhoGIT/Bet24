package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-04-29.
 */

public class Payments {

    int id_pay, id_user, value, accept;



    public Payments(int id_pay, int id_user, int value, int accept) {

        this.id_pay = id_pay;
        this.id_user = id_user;
        this.value = value;
        this.accept = accept;

    }

    public int getId_pay() {
        return id_pay;
    }

    public int getId_user() {
        return id_user;
    }

    public int getValue() {
        return value;
    }

    public int getAccept() {
        return accept;
    }
}
