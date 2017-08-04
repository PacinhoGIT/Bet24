package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-08-04.
 */

public class Reports {

    int id_user;
    int id_report;
    String title;
    String text;
    String date;
    String checked;
    String userName;

    public Reports(int id_report,int id_user,String userName, String title, String text, String date, String checked) {
        this.checked = checked;
        this.userName = userName;
        this.id_user = id_user;
        this.id_report = id_report;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public int getId_user() {
        return id_user;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int getId_report() {
        return id_report;
    }

    public String getChecked() {
        return checked;
    }
}
