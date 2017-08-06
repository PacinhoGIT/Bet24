package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-08-06.
 */

public class ReplyListCustomAdapter extends ArrayAdapter<Reply> {



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Reply reply = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_reply_to_raport_layout, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.shortTextReply);
        TextView date = (TextView) convertView.findViewById(R.id.shortDateReply);

        date.setText(reply.date);
        if(reply.getText().length()>20) {
            title.setText(reply.getText().substring(0, 20) + " ...");
        }
        else{
            title.setText(reply.getText());
        }

        if(reply.getChecked().equals("F")){
            title.setTypeface(Typeface.DEFAULT_BOLD);
            date.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else{
            title.setTypeface(Typeface.DEFAULT);
            date.setTypeface(Typeface.DEFAULT);
        }

        return convertView;
    }


    public ReplyListCustomAdapter(Context context, ArrayList<Reply> replies) {
        super(context, 0, replies);
    }



}