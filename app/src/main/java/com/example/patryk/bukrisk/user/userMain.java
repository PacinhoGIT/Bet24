package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetAllMatchesRequest;
import com.example.patryk.bukrisk.Request.GetBetsInCouponRequest;
import com.example.patryk.bukrisk.Request.GetCouponsToCheckRequest;
import com.example.patryk.bukrisk.Request.GetScoresRequest;
import com.example.patryk.bukrisk.Request.GetWalletRequest;
import com.example.patryk.bukrisk.Request.UpdateMatchesRequest;
import com.example.patryk.bukrisk.Request.UpdateSettledCouponsRequest;
import com.example.patryk.bukrisk.Request.updateWalletRequest;
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

    private ArrayList<String> failedCoupon;
    private ArrayList<String> couponWithNotFinishedMatches;




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
            failedCoupon = new ArrayList<>();
            couponWithNotFinishedMatches = new ArrayList<>();

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

                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(),"No coupons to check !",Toast.LENGTH_SHORT).show();

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

                    progressDialog.dismiss();

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

        betsAL.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

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
                            else if(!result.equals(type) && !result.equals("-")){

                                if(!failedCoupon.contains(id_coupon)){
                                    failedCoupon.add(id_coupon);
                                }
                            }
                        }

                        if(goodBet==betsAL.size())
                        {
                            goodCoupon.add(id_coupon);
                        }

                        if(end==true)
                        {
                            if(goodCoupon.size()>0 || failedCoupon.size()>0) {
                                showSummaryAlertDialog();
                                progressDialog.dismiss();
                            }else{
                                Toast.makeText(myView.getContext(),"No coupons to settled !",Toast.LENGTH_SHORT).show();
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

    private void showSummaryAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.checkCoupon);
        final View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.summary_check_coupon_alert_dialog, (ViewGroup) getView(), false);

        TextView countWinTV = (TextView) viewInflated.findViewById(R.id.countWinTV);
        TextView countLoseTV = (TextView) viewInflated.findViewById(R.id.countLoseTV);
        TextView moneyLoseTV = (TextView) viewInflated.findViewById(R.id.moneyLoseTV);
        TextView moneyWinTV = (TextView) viewInflated.findViewById(R.id.moneyWinTV);


        double moneyWin=0.0;
        double moneyLose=0.0;

        final double moneyWin2;
        final double moneyLose2;


        for(int i=0;i<goodCoupon.size();i++)
        {
            for(int j=0;j<coupons.size();j++)
            {
                if(goodCoupon.get(i).equals(coupons.get(j).getId_coupons())){

                    Double cValue = Double.valueOf(coupons.get(j).getTo_win().replace(",", "."));
                    moneyWin+=cValue;
                }
            }

        }

        moneyWin2=moneyWin;


        for(int i=0;i<failedCoupon.size();i++)
        {
            for(int j=0;j<coupons.size();j++)
            {
                if(failedCoupon.get(i).equals(coupons.get(j).getId_coupons())){

                    Double cValue = Double.valueOf(coupons.get(j).getMoney().replace(",", "."));
                    moneyLose+=cValue;
                }
            }

        }

        moneyLose2=moneyLose;

        countWinTV.setText(""+goodCoupon.size());
        countLoseTV.setText(""+failedCoupon.size());

        moneyWinTV.setText(""+moneyWin + " PLN");
        moneyLoseTV.setText(" - "+moneyLose + " PLN");

        builder.setView(viewInflated)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(moneyWin2>0) {
                            getWallet(moneyWin2, id);
                        }else if (moneyLose2>0){

                            updateFailedCoupons();
                        }
                        dialog.dismiss();

                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void getWallet(final double moneyWin, final int idUser) {


        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait. Update wallet ... ");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");


                    if (success) {

                        Double value = jsonResponse.getDouble("wallet");

                        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                      //  Double cValue = Double.valueOf(coupons.get(j).getMoney().replace(",", "."));
                        value = value + moneyWin;
                        String valueS= String.valueOf(value);

                         valueS = df.format(value);
                         valueS = valueS.replace(",", ".");


                        saveWallet(valueS, idUser);

                        //Toast.makeText(getContext(),""+value,Toast.LENGTH_SHORT).show();


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetWalletRequest walletRequest = new GetWalletRequest(""+idUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(walletRequest);

    }


    private void saveWallet(String value, int idUser) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        updateCoupons();

                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        updateWalletRequest uW = new updateWalletRequest("" + value, "" + idUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);
    }

    private void updateCoupons(){

        for(int i=0;i<goodCoupon.size();i++){

            final int i1 = i;

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {

                            if (i1 == goodCoupon.size() - 1) {

                                if(failedCoupon.size()>0)
                                {
                                    updateFailedCoupons();
                                }
                                else{
                                    Toast.makeText(myView.getContext(), "Succes !", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                               //
                            }

                        } else {
                        }

                    } catch (JSONException e) {

                        Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            };

            String id_coupon = goodCoupon.get(i);

            UpdateSettledCouponsRequest UMR = new UpdateSettledCouponsRequest(id_coupon,"Y", responseListener);
            RequestQueue queue = Volley.newRequestQueue(myView.getContext());
            queue.add(UMR);
        }
    }

    private void updateFailedCoupons(){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ... ");
        progressDialog.show();

        for(int i=0;i<failedCoupon.size();i++){

            final int i1 = i;

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {

                            if (i1 == failedCoupon.size() - 1) {

                                    Toast.makeText(myView.getContext(), "Succes !", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }


                        } else {
                        }

                    } catch (JSONException e) {

                        Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            };

            String id_coupon = failedCoupon.get(i);

            UpdateSettledCouponsRequest UMR = new UpdateSettledCouponsRequest(id_coupon,"Y", responseListener);
            RequestQueue queue = Volley.newRequestQueue(myView.getContext());
            queue.add(UMR);
        }

    }
}
