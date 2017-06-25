package com.example.patryk.bukrisk.user;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import com.example.patryk.bukrisk.Request.GetAllMatchesRequest;
import com.example.patryk.bukrisk.Request.GetBetsInCouponRequest;
import com.example.patryk.bukrisk.Request.GetCouponsToCheckRequest;
import com.example.patryk.bukrisk.Request.GetScoresRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Coupons;
import com.example.patryk.bukrisk.adapter.Matches;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-04-27.
 */

public class userMain extends Fragment
{
    View myView;

    private ProgressDialog progressDialog;
    private HashMap<Integer,String> matchResult;
    private HashMap<Integer,String> bets;

    private ArrayList<Bets>  betsAL;
    private ArrayList<String> goodCoupon;
    private ArrayList<Coupons> coupons ;

    private int id;




        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            myView = inflater.inflate(R.layout.user_home_layout, container, false);

            Intent intent = getActivity().getIntent();
            String name = intent.getStringExtra("name");
            String mail = intent.getStringExtra("mail");
            id = intent.getIntExtra("id_user",0);

            TextView tv1 = (TextView) myView.findViewById(R.id.userHomeTV);
            tv1.setText("Zalogowany jako : " + name + " " + mail);

            coupons = new ArrayList<>();
            betsAL = new ArrayList<>();
            goodCoupon = new ArrayList<>();

            matchResult = new HashMap<>();
            bets = new HashMap<>();

           getMatches();

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


                            Coupons c = new Coupons(""+id_coupon,""+to_win,""+total_course,""+id_user,""+name,""+money,""+date,""+risk,""+paid_off);

                            coupons.add(c);
                        }


                        if(coupons.size()>0) {
                            check();
                        }else{

                            progressDialog.dismiss();
                            Toast.makeText(myView.getContext(),"No coupons to check !",Toast.LENGTH_SHORT).show();
                        }




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

    private void getMatches() {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait. Check coupons ... ");
        progressDialog.show();

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

                            int idMatch = jsonValues.get(i).getInt("id_match");
                            String result = jsonValues.get(i).getString("result");

                            matchResult.put(idMatch,result);
                        }

                        getCouponsToCheck(id);


                    } else {



                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetAllMatchesRequest matchesRequest = new GetAllMatchesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void getBetsInCoupon(final String id_coupon, final boolean end){




        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        betsAL.clear();

                        JSONArray jsonArray = jsonResponse.getJSONArray("bets");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_match = jsonValues.get(i).getInt("id_match");
                            String type = jsonValues.get(i).getString("type");

                            Bets b = new Bets(""+id_match,"","",type,"","");
                            betsAL.add(b);

                        }

                        int goodBet=0;

                        for(int i=0;i<betsAL.size();i++){

                            Bets b = betsAL.get(i);

                            int id_match = Integer.parseInt(b.getId_match());
                            String type = b.getType();

                            String result = matchResult.get(id_match);

                            if(result.equals(type))
                            {
                                goodBet++;
                            }
                        }

                        if(goodBet==betsAL.size())
                        {
                            goodCoupon.add(id_coupon);
                        }

                        if(end==true)
                        {

                            if(goodCoupon.size()>0) {

                                String text = "Good coupon : ";
                                for(int i=0;i<goodCoupon.size();i++)
                                {
                                    text+=""+goodCoupon.get(i)+" ";
                                }
                                progressDialog.dismiss();
                                Toast.makeText(myView.getContext(),text, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(myView.getContext(),"No Win !", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {

                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),"Check Bets error !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        };

        GetBetsInCouponRequest matchesRequest = new GetBetsInCouponRequest(""+id_coupon,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void check(){

        for(int i=0;i<coupons.size();i++)
        {

            //Toast.makeText(myView.getContext(),"Check "+(i+1)+"/"+coupons.size(),Toast.LENGTH_SHORT).show();

            boolean end;

           Coupons c = coupons.get(i);

            String id_coupon = c.getId_coupons();

            if(i==coupons.size()-1)
            {
                end=true;
            }
            else{
                end=false;
            }
            getBetsInCoupon(id_coupon,end);

        }
    }
}
