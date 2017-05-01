package com.example.patryk.bukrisk.adapter;

/**
 * Created by patryk on 2017-04-30.
 */

public class Teams {

    int id_team;
    String name;
    int team_rating;
    int form_rating;
    double overall_rating;

    public Teams(int id_team, String name, int team_rating, int form_rating, double overall_rating) {
        this.id_team = id_team;
        this.name = name;
        this.team_rating = team_rating;
        this.form_rating = form_rating;
        this.overall_rating = overall_rating;
    }
}
