package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-05-16.
 */

public class NewCouponMatchesCustomAdapter extends ArrayAdapter<Matches> {


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Matches match = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.new_coupon_matches_custom_list_view, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.matchNewCouponTV);
        CheckedTextView A = (CheckedTextView) convertView.findViewById(R.id.CourseACheck);
        CheckedTextView X = (CheckedTextView) convertView.findViewById(R.id.CourseXCheck);
        CheckedTextView B = (CheckedTextView) convertView.findViewById(R.id.CourseBCheck);

        matchName.setText(match.match);
        A.setText("" + match.A);
        X.setText("" + match.X);
        B.setText("" + match.B);


        return convertView;
    }


    public NewCouponMatchesCustomAdapter(Context context, ArrayList<Matches> matches) {
        super(context, 0, matches);
    }
}