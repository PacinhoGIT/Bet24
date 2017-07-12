package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetAllUsersRequest;
import com.example.patryk.bukrisk.Request.GetFinishedMatchesRequest;
import com.example.patryk.bukrisk.Request.GetPaymentsRequest;
import com.example.patryk.bukrisk.Request.GetUserBetsRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.Payments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-04-27.
 */

public class adminMain extends Fragment{

    View myView;

    ProgressDialog progressDialog;
    String name;
    int usersCount=0;
    int payCount=0;
    int matchCount=0;

    TextView lastUser ;
    TextView userCount ;
    TextView paymentsToAcceptCount ;
    TextView scoreToSetCount ;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.admin_home_layout, container, false);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("name");

        TextView tv1 = (TextView) myView.findViewById(R.id.adminTV);
        lastUser = (TextView) myView.findViewById(R.id.lastUserTV);
        userCount = (TextView) myView.findViewById(R.id.totalUserTV);
        paymentsToAcceptCount = (TextView) myView.findViewById(R.id.paymentsToAcceptTV);
        scoreToSetCount = (TextView) myView.findViewById(R.id.scoreToSetTV);

        getUser();

        tv1.setText("Hello " + name + ". Have a nice day :)");


        return myView;
    }

    private void getUser(){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait a moment ...");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("users");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            name = jsonValues.get(i).getString("name");
                            usersCount++;
                        }

                        lastUser.setText("Last user : " + name);
                        userCount.setText("Total users count : " + usersCount);
                        getPayments();

                       // progressDialog.dismiss();


                    } else {

                    }

                } catch (JSONException e) {
                    //Toast.makeText(getContext(), "Bets " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };


        GetAllUsersRequest GAR = new GetAllUsersRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(GAR);

    }

    public void getPayments()
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("payment");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                           payCount++;

                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
                        }


                            paymentsToAcceptCount.setText("Payments to accept : " + payCount);
                            getFinishedMatches();

                        //progressDialog.dismiss();
                        //Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();


                    } else {

                        paymentsToAcceptCount.setText("Payments to accept : " + payCount);
                        getFinishedMatches();

                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetPaymentsRequest paymentsRequest = new GetPaymentsRequest("0", responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(paymentsRequest);

    }

    private void getFinishedMatches() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("matches");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            matchCount++;

                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
                        }

                        scoreToSetCount.setText("Matches to set score : " +matchCount);
                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(), "Have a nice day ! :)", Toast.LENGTH_SHORT).show();

                    } else {

                        scoreToSetCount.setText("Matches to set score : " +matchCount);
                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(), "Have a nice day ! :)", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetFinishedMatchesRequest matchesRequest = new GetFinishedMatchesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

}
