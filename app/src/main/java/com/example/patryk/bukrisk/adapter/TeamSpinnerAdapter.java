package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-04-30.
 */

public class TeamSpinnerAdapter  extends ArrayAdapter<Teams> {

    public TeamSpinnerAdapter(Context context, int resourses, ArrayList<Teams> teams) {
        super(context,resourses, teams);
    }

    private  Teams teams;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        teams = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_spinner_layout, parent, false);
        }

        TextView tv= (TextView) convertView.findViewById(R.id.teamSpinnerTV);

        tv.setText(teams.name);

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}