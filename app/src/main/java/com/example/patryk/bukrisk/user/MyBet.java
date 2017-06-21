package com.example.patryk.bukrisk.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetAllMatchesRequest;
import com.example.patryk.bukrisk.Request.GetScoresRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.Request.GetUserBetsRequest;
import com.example.patryk.bukrisk.Request.GetWalletRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.NewCouponMatchesCustomAdapter;
import com.example.patryk.bukrisk.adapter.ShowBetsInCouponCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-06-21.
 */

public class MyBet extends Fragment {

    private ProgressDialog progressDialog;

    HashMap<Integer, String> teamHash;
    HashMap<Integer, String> matchHash;

    ShowBetsInCouponCustomAdapter betsCustomAdapter;
    ArrayList<Bets> bets;

    private int id_user;

    TextView tv1;


    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.show_all_user_bets_layout, container, false);
        ListView betsLV = (ListView) myView.findViewById(R.id.allUserBetsLV);

        teamHash = new HashMap<>();
        matchHash = new HashMap<>();

        Bundle bundle = getArguments();
        id_user = bundle.getInt("id_user");

        bets = new ArrayList<>();

        Bets b = new Bets("", "Match", "", "Type", "Course/Risk", "");
        bets.add(b);

        betsCustomAdapter = new ShowBetsInCouponCustomAdapter(myView.getContext(), bets);
        betsLV.setAdapter(betsCustomAdapter);

        getTeam();

        return myView;
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


                        //Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();


                        getMatches();


                    } else {

                        progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

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

                       getBets();




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

    private void getBets()
    {
        //Toast.makeText(myView.getContext(), "Featching bets ... ",Toast.LENGTH_SHORT).show();

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

                            int match = jsonValues.get(i).getInt("id_match");
                            int id_coupon = jsonValues.get(i).getInt("id_coupon");
                            String type = jsonValues.get(i).getString("type");
                            Double course = jsonValues.get(i).getDouble("course");
                            Double risk = jsonValues.get(i).getDouble("risk");

                            String name = matchHash.get(match);

                            //Toast.makeText(myView.getContext(),""+match,Toast.LENGTH_SHORT).show();

                            Bets bet = new Bets("" + match, name, "" + id_coupon, type, "" + course, "" + risk + " %");

                            bets.add(bet);
                            betsCustomAdapter.notifyDataSetChanged();

                        }


                        progressDialog.dismiss();


                    } else {

                    }

                } catch (JSONException e) {
                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };


        GetUserBetsRequest walletRequest = new GetUserBetsRequest(""+id_user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(walletRequest);

    }
}

