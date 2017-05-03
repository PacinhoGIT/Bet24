package com.example.patryk.bukrisk.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-05-03.
 */

public class ShowMatchesCustomAdapter extends ArrayAdapter<Matches> {


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Matches match = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_macthes_custom_adapter_layout, parent, false);
        }

        TextView matchName = (TextView) convertView.findViewById(R.id.matchTvCustomAdapter);
        TextView A = (TextView) convertView.findViewById(R.id.courseATvCustomAdapter);
        TextView X = (TextView) convertView.findViewById(R.id.courseXTvCustomAdapter);
        TextView B = (TextView) convertView.findViewById(R.id.courseBTvCustomAdapter);

        matchName.setText(match.match);
        A.setText("" + match.A);
        X.setText("" + match.X);
        B.setText("" + match.B);


        return convertView;
    }


    public ShowMatchesCustomAdapter(Context context, ArrayList<Matches> matches) {
        super(context, 0, matches);
    }
}