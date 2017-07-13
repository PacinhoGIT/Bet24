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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-05-03.
 */

public class ShowMatchesCustomListAdapter extends ArrayAdapter<Matches> {

    Bitmap decodedByte;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Matches match = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_matches_custom_list_adapter_layout, parent, false);
        }

        TextView teamAName = (TextView) convertView.findViewById(R.id.teamAName_showMatches) ;
        TextView teamBName = (TextView) convertView.findViewById(R.id.teamBName_show_matches) ;

        TextView A = (TextView) convertView.findViewById(R.id.course_a_tv);
        TextView X = (TextView) convertView.findViewById(R.id.course_x_tv);
        TextView B = (TextView) convertView.findViewById(R.id.course_b_tv);

        ImageView logoA = (ImageView) convertView.findViewById(R.id.teamALogo_howMatches);
        ImageView logoB = (ImageView) convertView.findViewById(R.id.teamBLogo_showMatches);

        teamAName.setText(""+match.teamA);
        teamBName.setText(""+match.teamB);
        A.setText("" + match.A);
        X.setText("" + match.X);
        B.setText("" + match.B);

        if(match.logoA.length()>0){

            logoA.setVisibility(View.VISIBLE);
            decodeByte64(match.logoA);
            logoA.setImageBitmap(decodedByte);
        }else{
           logoA.setVisibility(View.INVISIBLE);
        }

        if(match.logoA.length()>0) {
            logoB.setVisibility(View.VISIBLE);
            decodeByte64(match.logoB);
            logoB.setImageBitmap(decodedByte);
        }
        else{
            logoB.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }


    public ShowMatchesCustomListAdapter(Context context, ArrayList<Matches> matches) {
        super(context, 0, matches);
    }
}