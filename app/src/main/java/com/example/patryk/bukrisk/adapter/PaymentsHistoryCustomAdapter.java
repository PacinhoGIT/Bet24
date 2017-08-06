package com.example.patryk.bukrisk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class PaymentsHistoryCustomAdapter  extends ArrayAdapter<Payments> {

    Bitmap decodedByte;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        Payments paymentses = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.payments_history_list_view_layout, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.payValue);
        TextView date = (TextView) convertView.findViewById(R.id.payDate);
        TextView read = (TextView) convertView.findViewById(R.id.payAccept);


        if(position>0) {
            title.setText(paymentses.getValue());
            title.setTypeface(Typeface.DEFAULT);
            date.setTypeface(Typeface.DEFAULT);
            read.setTypeface(Typeface.DEFAULT);
        }
        else{
            title.setTypeface(Typeface.DEFAULT_BOLD);
            date.setTypeface(Typeface.DEFAULT_BOLD);
            read.setTypeface(Typeface.DEFAULT_BOLD);
            title.setText(paymentses.getValue() + " [PLN]");
            read.setText("Status");
        }
        date.setText(paymentses.getDate());
        if(paymentses.getAccept().equals("0")){
            read.setText("To accept");
            read.setTextColor(Color.BLACK);
        }else if(paymentses.getAccept().equals("10")){
            read.setText("Declined");
            read.setTextColor(Color.RED);

        }
        else if(paymentses.getAccept().equals("11")) {
            read.setText("Accepted");
            read.setTextColor(Color.GREEN);
        }


        return convertView;
    }


    public PaymentsHistoryCustomAdapter(Context context, ArrayList<Payments> paymentses) {
        super(context, 0, paymentses);
    }
}
