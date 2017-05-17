package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-05-03.
 */

public class ShowBetsInCouponCustomAdapter extends ArrayAdapter<Bets> {


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Bets bets = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bets_custom_list_view, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.betMatch);
        TextView type = (TextView) convertView.findViewById(R.id.userType);
        TextView typeCourse = (TextView) convertView.findViewById(R.id.typeCourse);

        matchName.setText(bets.name);
        type.setText("" + bets.type);
        typeCourse.setText("" + bets.course);

        return convertView;
    }


    public ShowBetsInCouponCustomAdapter(Context context, ArrayList<Bets> bets) {
        super(context, 0, bets);
    }
}