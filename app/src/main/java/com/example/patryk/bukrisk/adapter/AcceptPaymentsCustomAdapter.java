package com.example.patryk.bukrisk.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetWalletRequest;
import com.example.patryk.bukrisk.Request.updatePaymentsRequest;
import com.example.patryk.bukrisk.Request.updateWalletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-04-28.
 */

public class AcceptPaymentsCustomAdapter extends ArrayAdapter<Payments> {

    double value;

    int pay=0;
    int id_u=0;
    int id_pay=0;
    int position1;


    ProgressDialog progressDialog;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Payments uPay = getItem(position);

        pay = uPay.value;
        id_u = uPay.id_user;


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_adapter_accept_payments_layout, parent, false);
        }

        TextView tvIdpay = (TextView) convertView.findViewById(R.id.tvID_pay);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvID_user);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
        Button accept = (Button) convertView.findViewById(R.id.customAdapterAcceptBTN);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                value=0.0;
                id_u=0;
                id_pay=0;

                Payments uPay = getItem(position);
                position1 = position;

                pay = uPay.value;
                id_u = uPay.id_user;
                id_pay = uPay.id_pay;



                //Toast.makeText(getContext(),""+pay,Toast.LENGTH_SHORT).show();


                progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please Wait !");
                progressDialog.show();

                getWallet(id_u);

                //Toast.makeText(getContext(),""+value,Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(),""+value,Toast.LENGTH_SHORT).show();




            }
        });

        tvIdpay.setText(""+uPay.id_pay);
        tvId.setText(""+uPay.id_user);
        tvValue.setText(""+uPay.value);


        return convertView;
    }


    public AcceptPaymentsCustomAdapter(Context context, ArrayList<Payments> UserPayments) {
        super(context, 0, UserPayments);
    }

    private void getWallet(int idUser)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {

                        value = jsonResponse.getDouble("wallet");
                        value = value + pay;

                        saveWallet(value,id_u);

                        //Toast.makeText(getContext(),""+value,Toast.LENGTH_SHORT).show();



                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        GetWalletRequest walletRequest = new GetWalletRequest(""+idUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(walletRequest);

    }

    private void saveWallet(double value, int idUser) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        updatePayments(id_pay);

                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        updateWalletRequest uW = new updateWalletRequest("" + value, "" + idUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(uW);
    }

    private void updatePayments(int id_pay)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        Payments uPay = getItem(position1);
                        remove(uPay);
                        notifyDataSetChanged();

                        progressDialog.dismiss();

                        new AlertDialog.Builder(getContext())
                                .setTitle("Added funds")
                                .setMessage("Success !")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }

                                })
                                .show();
                    } else {

                        progressDialog.dismiss();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        updatePaymentsRequest uW = new updatePaymentsRequest(""+id_pay, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(uW);
    }
}
