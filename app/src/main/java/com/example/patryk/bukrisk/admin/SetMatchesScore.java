package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetFinishedMatchesRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.Request.UpdateMatchesScoreRequest;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.NewCouponMatchesCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patryk on 2017-06-12.
 */

public class SetMatchesScore extends Fragment {

    private ProgressDialog progressDialog;
    private ListView matchesLV;
    private ArrayList<Matches> matches;
    private NewCouponMatchesCustomAdapter matchesCustomAdapter;

    HashMap<Integer, String> teamHash;
    HashMap<Integer, String> teamLogoHash;
    HashMap<String, Integer> matchHash;

    Matches match;

    Bitmap decodedByte;

    int position1;

    View myView;


    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.set_matches_score_layout, container, false);

        matchesLV = (ListView) myView.findViewById(R.id.set_score_lv);

        matches = new ArrayList<>();
        matchesCustomAdapter = new NewCouponMatchesCustomAdapter(myView.getContext(), matches);
        matchesLV.setAdapter(matchesCustomAdapter);

        teamHash = new HashMap<>();
        teamLogoHash = new HashMap<>();
        matchHash = new HashMap<>();

        getTeam();

        matchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                position1=position;

                Matches m = matches.get(position);

                String id = m.getId().toString();
                String nameM = m.getMatch().toString();
                String dateM = m.getDate().toString();
                String homeM = m.getA().toString();
                String drawM = m.getX().toString();
                String awayM = m.getB().toString();

                String logoA = m.getLogoA().toString();
                String logoB = m.getLogoB().toString();

                //Toast.makeText(myView.getContext(),nameM + dateM + homeM + drawM + awayM ,Toast.LENGTH_SHORT).show();
                setMatchesScore(id, nameM, dateM,logoA,logoB);
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
                            String logo = jsonValues.get(i).getString("logo");

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

                            match = new Matches("" + idMatch, matchName,teamAName,logoA,teamBName,logoB,"", "" + teamA, "" + draw, "" + teamB, data);
                            matches.add(match);


                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
                        }

                        matchesCustomAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(), R.string.success, Toast.LENGTH_SHORT).show();





                    } else {

                        if (matches.size() == 0) {

                            final AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
                            alertDialog.setTitle("Set matches score");
                            alertDialog.setMessage("Not available macthes !");

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

        GetFinishedMatchesRequest matchesRequest = new GetFinishedMatchesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

    private void setMatchesScore(final String id, String name, String date, String logoA, String logoB)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.setMatchesScore);
        final View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.set_matches_score_alert_dialog_layout, (ViewGroup) getView(), false);

        final TextView teamA = (TextView) viewInflated.findViewById(R.id.teamAAlertDialog);
        final TextView teamB = (TextView) viewInflated.findViewById(R.id.teamBAlertDialog);
        final TextView matchName = (TextView) viewInflated.findViewById(R.id.matchNameAlertDialog);
        final TextView matchDate = (TextView) viewInflated.findViewById(R.id.matchDateAlertDialog);

        final EditText teamAScoreValue = (EditText) viewInflated.findViewById(R.id.resultAAlertDialogET);
        final EditText teamBScoreValue = (EditText) viewInflated.findViewById(R.id.resultBAlertDialogET);

        final ImageView logoAlogo = (ImageView) viewInflated.findViewById(R.id.teamALogosetScoreIV);
        final ImageView logoBlogo = (ImageView) viewInflated.findViewById(R.id.teamBLogoSetScoreIV);

        matchName.setText(name);
        matchDate.setText(date);

        String teamA_S = name.split("-")[0];
        String teamB_S = name.substring(name.lastIndexOf("-") + 1);

        teamA.setText(teamA_S);
        teamB.setText(teamB_S);

        decodeByte64(logoA);
        logoAlogo.setImageBitmap(decodedByte);

        decodeByte64(logoB);
        logoBlogo.setImageBitmap(decodedByte);



        builder.setView(viewInflated)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String result="";

                            int teamAScore = Integer.parseInt(teamAScoreValue .getText().toString());
                            int teamBScore = Integer.parseInt(teamBScoreValue.getText().toString());

                            if (teamAScore > teamBScore) {
                                result = "A";
                            } else if (teamAScore < teamBScore) {
                                result = "B";
                            } else if (teamAScore == teamBScore) {
                                result = "X";
                            }

                            String score = teamAScore + " : " + teamBScore;

                            updateMatchesScore(id,score,result);

                           // Toast.makeText(myView.getContext(),score,Toast.LENGTH_SHORT).show();

                    }
                });
        builder.setView(viewInflated)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateMatchesScore(String id, String score, String result){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Update matches ... ");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        progressDialog.dismiss();

                        new AlertDialog.Builder(myView.getContext())
                                .setTitle("Set Score")
                                .setMessage("Success !")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        matches.remove(position1);
                                        matchesCustomAdapter.notifyDataSetChanged();

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

        UpdateMatchesScoreRequest uW = new UpdateMatchesScoreRequest(id,score,result, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);
    }
}
