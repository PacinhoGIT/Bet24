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
 * Created by patryk on 2017-06-23.
 */

public class ShowCouponsCustomAdapter extends ArrayAdapter<Coupons> {


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Coupons coupons = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bets_custom_list_view, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.betMatch);
        TextView type = (TextView) convertView.findViewById(R.id.userType);
        TextView typeCourse = (TextView) convertView.findViewById(R.id.typeCourse);
        // TextView risk = (TextView) convertView.findViewById(R.id.riskTV);

        matchName.setText(coupons.name);
        type.setText("" + coupons.to_win);
        typeCourse.setText("" + coupons.risk+" %");
        //risk.setText("" + bets.risk);

        return convertView;
    }


    public ShowCouponsCustomAdapter(Context context, ArrayList<Coupons> coupons) {
        super(context, 0, coupons);
    }
}