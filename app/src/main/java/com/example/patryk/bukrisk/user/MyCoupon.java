package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetAllMatchesRequest;
import com.example.patryk.bukrisk.Request.GetBetsInCouponRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.Request.GetUserBetsRequest;
import com.example.patryk.bukrisk.Request.GetUserCouponsRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Coupons;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.ShowBetsInCouponCustomAdapter;
import com.example.patryk.bukrisk.adapter.ShowCouponsCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-06-23.
 */

public class MyCoupon  extends Fragment {

    private ProgressDialog progressDialog;

    ShowCouponsCustomAdapter couponsAdapter;
    ArrayList<Coupons> coupons;
    ArrayList<Bets> betsAL;

    HashMap<Integer, String> teamHash;
    HashMap<Integer, String> matchHash;


    private int id_user;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.show_all_user_bets_layout, container, false);
        ListView couponsLV = (ListView) myView.findViewById(R.id.allUserBetsLV);

        coupons = new ArrayList<>();
        teamHash = new HashMap<>();
        matchHash = new HashMap<>();

        Coupons c = new Coupons("", "To Win", "", "", "Name", "","","Risk","");
        coupons.add(c);
        couponsAdapter = new ShowCouponsCustomAdapter(myView.getContext(), coupons);
        couponsLV.setAdapter(couponsAdapter);

        Bundle bundle = getArguments();
        id_user = bundle.getInt("id_user");

        getTeam();

        //getCoupons();

        couponsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                if(position>0) {
                    Coupons c = coupons.get(position);

                    String id = c.getId_coupons().toString();
                    String name = c.getName().toString();
                    String money = c.getMoney().toString();
                    String date = c.getDate().toString();
                    String total_course = c.getTotal_course().toString();
                    String risk = c.getRisk().toString();
                    String to_win = c.getTo_win().toString();
                    String paid_off = c.getPaid_off().toString();

                    showCouponDetail(id,name,money,date,total_course,risk,to_win,paid_off);

                }

            }
        });

        return myView;
    }

    private void showCouponDetail(final String id, String name, String money, String date, String total_course, String risk,String to_win,String paid_off){


        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.couponDetail);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.coupon_detail_custom_alert_dialog, (ViewGroup) getView(), false);

        final TextView nameTV = (TextView) viewInflated.findViewById(R.id.couponsDetail_Name);
        final TextView dateTV = (TextView) viewInflated.findViewById(R.id.couponsDetail_Date);
        final TextView moneyTV = (TextView) viewInflated.findViewById(R.id.couponsDetail_money);
        final TextView total_courseTV = (TextView) viewInflated.findViewById(R.id.couponsDetail_total_course);
        final TextView riskTV= (TextView) viewInflated.findViewById(R.id.couponsDetail_total_risk);
        final TextView to_winTV= (TextView) viewInflated.findViewById(R.id.couponsDetail_to_win);
        final TextView paidTV= (TextView) viewInflated.findViewById(R.id.couponsDetail_paid_off);

        nameTV.setText(name);
        dateTV.setText(date);
        moneyTV.setText(money + " PLN");
        total_courseTV.setText(total_course);
        riskTV.setText(risk + " %");
        to_winTV.setText(to_win + " PLN");

        if(paid_off.equals("F")){
            paid_off="No Settled";
        }else if(paid_off.equals("Y/W")){
            paid_off="Yes";
        }else if(paid_off.equals("Y/L")){
            paid_off="No";
        }

        paidTV.setText(paid_off);


        builder.setView(viewInflated)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                    }
                });

        builder.setNeutralButton(R.string.betInCoupon, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getBetsInCoupon(id);
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void getTeam() {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data from the Server.");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        JSONArray jsonArray = jsonResponse.getJSONArray("teams");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_team = jsonValues.get(i).getInt("id_team");
                            String name = jsonValues.get(i).getString("name");

                            teamHash.put(id_team, name);

                        }


                        getMatches();


                    } else {

                        progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),"Team " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetTeamRequest gTR = new GetTeamRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(gTR);
    }

    private void getMatches() {

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
                            int id_home = jsonValues.get(i).getInt("id_home");
                            int id_away = jsonValues.get(i).getInt("id_away");
                            String score = jsonValues.get(i).getString("score");
                            String data = jsonValues.get(i).getString("date");
                            double teamA = jsonValues.get(i).getDouble("teamA");
                            double draw = jsonValues.get(i).getDouble("draw");
                            double teamB = jsonValues.get(i).getDouble("teamB");

                            String teamAName = teamHash.get(id_home);
                            String teamBName = teamHash.get(id_away);

                            String matchName = teamAName + " - " + teamBName;
                            matchHash.put(idMatch, matchName);

                        }


                        getCoupons();




                    } else {

                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),"Match " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetAllMatchesRequest matchesRequest = new GetAllMatchesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void getCoupons()
    {
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
                            String name = jsonValues.get(i).getString("name");
                            Double money = jsonValues.get(i).getDouble("money");
                            String date = jsonValues.get(i).getString("date");
                            Double total_course = jsonValues.get(i).getDouble("total_course");
                            Double risk = jsonValues.get(i).getDouble("risk");
                            Double to_win= jsonValues.get(i).getDouble("to_win");
                            String paid_off= jsonValues.get(i).getString("paid_off");

                            Coupons c = new Coupons(""+id_coupon,""+to_win,""+total_course,"",name,""+money,date,""+risk,paid_off);
                            coupons.add(c);

                            couponsAdapter.notifyDataSetChanged();
                        }


                        progressDialog.dismiss();


                    } else {

                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {
                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };


        GetUserCouponsRequest walletRequest = new GetUserCouponsRequest(""+id_user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(walletRequest);
    }

    private void getBetsInCoupon(final String id_coupon){


        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching data from the Server.");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        betsAL = new ArrayList<>();
                        betsAL.clear();

                        Bets b = new Bets("", "Match", "", "Type", "Course/Risk", "");
                        betsAL.add(b);

                        JSONArray jsonArray = jsonResponse.getJSONArray("bets");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_match = jsonValues.get(i).getInt("id_match");
                            String type = jsonValues.get(i).getString("type");
                            Double course = jsonValues.get(i).getDouble("course");
                            Double risk = jsonValues.get(i).getDouble("risk");
                            String name = matchHash.get(id_match);

                            Bets b1 = new Bets(""+id_match,name,"",type,""+course,""+risk);
                            betsAL.add(b1);

                        }

                        showBetsAlertDialog();

                    } else {


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),"Get Bets error !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        };

        GetBetsInCouponRequest matchesRequest = new GetBetsInCouponRequest(""+id_coupon,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void showBetsAlertDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.betInCoupon);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.show_all_user_bets_layout, (ViewGroup) getView(), false);
        ListView betsLV = (ListView) viewInflated.findViewById(R.id.allUserBetsLV);

        ShowBetsInCouponCustomAdapter betsCustomAdapter;

        betsCustomAdapter = new ShowBetsInCouponCustomAdapter(myView.getContext(), betsAL);
        betsLV.setAdapter(betsCustomAdapter);

        builder.setView(viewInflated)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        progressDialog.dismiss();

                    }
                });

        android.app.AlertDialog alert = builder.create();
        alert.show();

    }
}

