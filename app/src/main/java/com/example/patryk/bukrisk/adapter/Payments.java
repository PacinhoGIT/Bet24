package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-04-29.
 */

public class Payments {

    String id_pay, id_user, value, accept;
    String name;



    public Payments(String id_pay, String id_user,String name, String value, String accept) {

        this.id_pay = id_pay;
        this.id_user = id_user;
        this.name = name;
        this.value = value;
        this.accept = accept;

    }

    public String getId_user() {return id_user;}

    public String getId_pay() {
        return id_pay;
    }

    public String getName() {return name;}

    public String getValue() {
        return value;
    }

    public String getAccept() {
        return accept;
    }
}
