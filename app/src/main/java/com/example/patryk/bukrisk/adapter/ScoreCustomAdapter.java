package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by patryk on 2017-07-12.
 */

public class ScoreCustomAdapter extends ArrayAdapter<Matches> {

    Bitmap decodedByte;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Matches match = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.new_coupon_matches_custom_list_view, parent, false);
        }

        TextView teamA = (TextView) convertView.findViewById(R.id.teamALogoNameTV);
        TextView teamB = (TextView) convertView.findViewById(R.id.teamBLogoNameTV);
        TextView score = (TextView) convertView.findViewById(R.id.scoreTV);

        teamA.setVisibility(View.INVISIBLE);
        teamB.setVisibility(View.INVISIBLE);

        ImageView teamALogo = (ImageView) convertView.findViewById(R.id.teamALogoIV);
        ImageView teamBLogo = (ImageView) convertView.findViewById(R.id.teamBLogoIV);

        //TextView date = (TextView) convertView.findViewById(R.id.dateMatchTV);

        //teamA.setText(match.teamA);
        //teamB.setText(match.teamB);

        decodeByte64(match.logoA);
        teamALogo.setImageBitmap(decodedByte);

        score.setText("" + match.score);

        decodeByte64(match.logoB);
        teamBLogo.setImageBitmap(decodedByte);

        return convertView;
    }


    public ScoreCustomAdapter(Context context, ArrayList<Matches> matches) {
        super(context, 0, matches);
    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

}
