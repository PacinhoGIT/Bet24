package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    Bitmap decodedByte;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        teams = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_spinner_layout, parent, false);
        }

        TextView tv= (TextView) convertView.findViewById(R.id.teamSpinnerTV);
        ImageView logo = (ImageView) convertView.findViewById(R.id.teamLogoImage);

        tv.setText(teams.name);

        if(teams.logo.length()>0) {
            decodeByte64(teams.logo);
            logo.setImageBitmap(decodedByte);
        }
        else
        {
            logo.setImageResource(R.drawable.team);
        }




        return convertView;
    }




    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

}