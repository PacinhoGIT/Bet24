package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetFinishedMatchesRequest;
import com.example.patryk.bukrisk.Request.GetScoresRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.NewCouponMatchesCustomAdapter;
import com.example.patryk.bukrisk.adapter.ScoreCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by patryk on 2017-06-15.
 */

public class ShowScore extends Fragment {

    private ProgressDialog progressDialog;
    private ListView matchesLV;
    private ArrayList<Matches> matches;
    private ScoreCustomAdapter matchesCustomAdapter;

    HashMap<Integer, String> teamHash;
    HashMap<Integer, String> teamLogoHash;
    HashMap<String, Integer> matchHash;

    TextView tv1;

    String day="7";

    Matches match;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.set_matches_score_layout, container, false);

       // tv1 = (TextView) myView.findViewById(R.id.setScoreTV);
       // tv1.setText("Scores");

        matchesLV = (ListView) myView.findViewById(R.id.matchesLV);
        Spinner dayCount = (Spinner) myView.findViewById(R.id.dayCountSpinner);

        List<String> days = new ArrayList<String>();
        days.add("--Days--");
        days.add("7 days");
        days.add("14 days");
        days.add("31 days");

        matches = new ArrayList<>();




        matchesCustomAdapter = new ScoreCustomAdapter(myView.getContext(), matches);
        matchesLV.setAdapter(matchesCustomAdapter);

        teamHash = new HashMap<>();
        teamLogoHash = new HashMap<>();
        matchHash = new HashMap<>();

      /*  match = new Matches("","","","","","","", "","","","");
        matches.add(match);
        matchesCustomAdapter.notifyDataSetChanged();*/

        getTeam();

        matchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(myView.getContext(), matches.get(position).getMatch(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myView.getContext(), android.R.layout.simple_spinner_item, days);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayCount.setAdapter(dataAdapter);

        dayCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0) {

                    teamHash.clear();
                    teamLogoHash.clear();
                    matchHash.clear();
                    matches.clear();

                    matchesCustomAdapter.notifyDataSetChanged();

                    if (position == 1) {
                        day = "7";
                        getTeam();
                    } else if (position == 2) {
                        day = "14";
                        getTeam();
                    } else if (position == 3) {
                        day = "31";
                        getTeam();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                day="7";
            }

        });

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
                            String logo= jsonValues.get(i).getString("logo");

                            teamHash.put(id_team, name);
                            teamLogoHash.put(id_team, logo);

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

                            String logoA = teamLogoHash.get(id_home);
                            String logoB = teamLogoHash.get(id_away);

                            String matchName = teamAName + " - " + teamBName;
                            matchHash.put(matchName, idMatch);

                            match = new Matches("" + idMatch, matchName+"\n "+data,teamAName,logoA,teamBName,logoB,score, "" + teamA, "" + draw, "" + teamB, data);
                            matches.add(match);


                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
                        }

                        matchesCustomAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(), R.string.success, Toast.LENGTH_SHORT).show();




                    } else {

                        if (matches.size() == 1) {

                            final AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
                            alertDialog.setTitle("Show Score");
                            alertDialog.setMessage("Not available data !");

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    alertDialog.dismiss();
                                }
                            });

                            alertDialog.show();

                            progressDialog.dismiss();
                            Toast.makeText(myView.getContext(), R.string.success, Toast.LENGTH_SHORT).show();


                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(myView.getContext(), R.string.success, Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetScoresRequest matchesRequest = new GetScoresRequest(day,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }
}
