package com.example.patryk.bukrisk.user;

import android.app.Fragment;
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
import com.example.patryk.bukrisk.Request.GetCouponsToCheckRequest;
import com.example.patryk.bukrisk.adapter.Coupons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-04-27.
 */

public class userMain extends Fragment
{
    View myView;

    ArrayList<Coupons> coupons ;

        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            myView = inflater.inflate(R.layout.user_home_layout, container, false);

            Intent intent = getActivity().getIntent();
            String name = intent.getStringExtra("name");
            String mail = intent.getStringExtra("mail");
            int id = intent.getIntExtra("id_user",0);

            TextView tv1 = (TextView) myView.findViewById(R.id.userHomeTV);
            tv1.setText("Zalogowany jako : " + name + " " + mail);

            coupons = new ArrayList<>();

           // getCouponsToCheck(id);

            return myView;

        }

    private void getCouponsToCheck(int id_user){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("coupons");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_coupon = jsonValues.get(i).getInt("id_coupon");
                            int id_user = jsonValues.get(i).getInt("id_user");
                            String name = jsonValues.get(i).getString("name");
                            Double money = jsonValues.get(i).getDouble("money");
                            String date = jsonValues.get(i).getString("date");
                            Double total_course = jsonValues.get(i).getDouble("total_course");
                            Double risk = jsonValues.get(i).getDouble("risk");
                            Double to_win = jsonValues.get(i).getDouble("to_win");
                            String paid_off = jsonValues.get(i).getString("paid_off");


                            Coupons c = new Coupons(""+id_coupon,""+id_user,name,""+money,""+date,""+total_course,""+risk,""+to_win,paid_off);

                            coupons.add(c);
                        }


                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();


                    } else {



                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetCouponsToCheckRequest couponsRequest = new GetCouponsToCheckRequest(""+id_user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(couponsRequest);
    }
}
