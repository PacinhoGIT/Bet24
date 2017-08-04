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
    String logo;
    String liga;

    public Teams(int id_team, String name, int team_rating, int form_rating, double overall_rating, String logo, String liga) {
        this.id_team = id_team;
        this.name = name;
        this.team_rating = team_rating;
        this.form_rating = form_rating;
        this.overall_rating = overall_rating;
        this.logo=logo;
        this.liga=liga;
    }

    public String getLiga() {
        return liga;
    }

    public int getId_team() {
        return id_team;
    }

    public String getName() {
        return name;
    }

    public int getTeam_rating() {
        return team_rating;
    }

    public int getForm_rating() {
        return form_rating;
    }

    public double getOverall_rating() {
        return overall_rating;
    }

    public String getLogo() {
        return logo;
    }
}
