package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.Telephony;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-08-04.
 */

public class UserReportsCustomAdapter extends ArrayAdapter<Reports> {



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Reports reports = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_reports_list_layout, parent, false);
        }
        ImageView newIcon = (ImageView) convertView.findViewById(R.id.newIcon);

        TextView title = (TextView) convertView.findViewById(R.id.reportTitle2);
        TextView date = (TextView) convertView.findViewById(R.id.reportDate2);
        TextView read = (TextView) convertView.findViewById(R.id.reportRead);

        title.setText(reports.getTitle());
        date.setText(reports.getDate());

        if(position==0){
            title.setTextSize(16);
            title.setTypeface(Typeface.DEFAULT_BOLD);

            date.setTextSize(16);
            date.setTypeface(Typeface.DEFAULT_BOLD);

            read.setTextSize(16);
            read.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if(reports.getChecked().equals("F"))
        {
            read.setText("No");
            read.setTextColor(Color.RED);
        }
        else if(reports.getChecked().equals("Y")){
            read.setTextColor(Color.GREEN);

            read.setText("Yes");
        }
        else{
            read.setText("Read");
        }



        return convertView;
    }


    public UserReportsCustomAdapter(Context context, ArrayList<Reports> reportses) {
        super(context, 0, reportses);
    }



}