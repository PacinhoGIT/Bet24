package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ReportsCustomAdapter extends ArrayAdapter<Reports> {



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Reports reports = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reports_list_view_layout, parent, false);
        }

        TextView user = (TextView) convertView.findViewById(R.id.reportUser);
        TextView title = (TextView) convertView.findViewById(R.id.reportTitle);
        TextView date = (TextView) convertView.findViewById(R.id.reportDate);

        ImageView newIcon = (ImageView) convertView.findViewById(R.id.newIcon);

        user.setText(reports.userName);
        title.setText(reports.title);
        date.setText(reports.date);

        if(reports.checked.equals("F")){

            user.setTypeface(null, Typeface.BOLD_ITALIC);
            user.setTextSize(16);
            title.setTypeface(null, Typeface.BOLD_ITALIC);
            title.setTextSize(16);
            date.setTypeface(null, Typeface.BOLD_ITALIC);
            date.setTextSize(16);
        }
        else{
            user.setTypeface(null, Typeface.NORMAL);
            user.setTextSize(14);
            title.setTypeface(null, Typeface.NORMAL);
            title.setTextSize(14);
            date.setTypeface(null, Typeface.NORMAL);
            date.setTextSize(14);

            newIcon.setVisibility(View.INVISIBLE);
        }




        return convertView;
    }


    public ReportsCustomAdapter(Context context, ArrayList<Reports> reportses) {
        super(context, 0, reportses);
    }



}