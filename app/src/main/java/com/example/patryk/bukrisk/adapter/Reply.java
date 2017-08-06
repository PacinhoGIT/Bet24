package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-08-05.
 */

public class Reply {

    int id_reply;
    int id_report;
    String text;
    String checked;
    String date;

    public Reply(int id_reply, int id_report, String text, String checked,String date) {
        this.id_reply = id_reply;
        this.id_report = id_report;
        this.text = text;
        this.checked = checked;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getChecked() {
        return checked;
    }

    public int getId_reply() {
        return id_reply;
    }

    public int getId_report() {
        return id_report;
    }

    public String getText() {
        return text;
    }
}
