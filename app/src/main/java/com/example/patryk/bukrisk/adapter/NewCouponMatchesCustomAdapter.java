package com.example.patryk.bukrisk.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.admin.SetMatchesScore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-05-16.
 */

public class NewCouponMatchesCustomAdapter extends ArrayAdapter<Matches> {

        Bitmap decodedByte;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Matches match = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.new_coupon_matches_custom_list_view, parent, false);
        }

        TextView teamA = (TextView) convertView.findViewById(R.id.teamALogoNameTV);
        TextView teamB = (TextView) convertView.findViewById(R.id.teamBLogoNameTV);

        ImageView teamALogo = (ImageView) convertView.findViewById(R.id.teamALogoIV);
        ImageView teamBLogo = (ImageView) convertView.findViewById(R.id.teamBLogoIV);

        //TextView date = (TextView) convertView.findViewById(R.id.dateMatchTV);

        teamA.setText(match.teamA);
        teamB.setText(match.teamB);

        decodeByte64(match.logoA);
        teamALogo.setImageBitmap(decodedByte);

        decodeByte64(match.logoB);
        teamBLogo.setImageBitmap(decodedByte);

        return convertView;
    }


    public NewCouponMatchesCustomAdapter(Context context, ArrayList<Matches> matches) {
        super(context, 0, matches);
    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

}