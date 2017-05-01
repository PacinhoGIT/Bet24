package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.adapter.TeamSpinnerAdapter;
import com.example.patryk.bukrisk.adapter.Teams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by patryk on 2017-04-30.
 */

public class addMatch extends Fragment {

    View myView;

    Button addBTN, calculateCurse;
    ImageButton dateBTN;
    Spinner teamASpin;
    Spinner teamBSpin;
    TextView dateTV,curseATV, curseDrawTV,curseBTV;

    Teams team;

    ArrayList<Teams> TeamsList;
    ArrayList<String> teamsLits;
    HashMap<String, Integer> teamsHashMap;

    ProgressDialog progressDialog;

    private int year;
    private int month;
    private int day;

    String month1;
    String day1;
    String teamA,teamB;

    TeamSpinnerAdapter adapter;
    TeamSpinnerAdapter adapter2;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.add_match_layout, container, false);

        addBTN = (Button) myView.findViewById(R.id.addTeamBTN);
        dateBTN = (ImageButton) myView.findViewById(R.id.dateImgBtn);
        calculateCurse = (Button) myView.findViewById(R.id.calculateCurseBTN);

        teamASpin = (Spinner) myView.findViewById(R.id.teamASpin);
        teamBSpin = (Spinner) myView.findViewById(R.id.teamBSpin);

        dateTV = (TextView) myView.findViewById(R.id.dateTV);
        curseATV = (TextView) myView.findViewById(R.id.curseATV);
        curseBTV = (TextView) myView.findViewById(R.id.curseBTV);
        curseDrawTV = (TextView) myView.findViewById(R.id.curseDrawTV);

        TeamsList = new ArrayList<Teams>();
        teamsLits = new ArrayList<String>();


        teamsHashMap = new HashMap<>();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        month += 1;

        if (month < 10) {
            month1 = "0" + month;
        }

        if (day < 10) {
            day1 = "0" + day;
        }
        else
        {
            day1=""+day;
        }


        dateTV.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year)
                .append("-")
                .append(month1)
                .append("-")
                .append(day1));

        dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                if (month < 9) {
                    month1 = "0" + month;
                }

                DatePickerDialog dpd = new DatePickerDialog(myView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String monthS = "";
                                monthOfYear += 1;

                                if (monthOfYear < 10) {
                                    monthS = "0" + monthOfYear;
                                } else {
                                    monthS = Integer.toString(monthOfYear);
                                }

                                if (dayOfMonth < 10) {
                                    day1 = "0" + dayOfMonth;
                                } else {
                                    day1 = Integer.toString(dayOfMonth);
                                }


                                dateTV.setText(year + "-"
                                        + (monthS) + "-" + day1);

                            }
                        }, year, month, day);
                dpd.show();

            }
        });

        getTeam();

        //TSA = new TeamSpinnerAdapter(myView.getContext(),R.layout.team_spinner_layout,TeamsList);

        adapter = new TeamSpinnerAdapter(myView.getContext(),
               R.layout.team_spinner_layout, TeamsList);

        adapter2 = new TeamSpinnerAdapter(myView.getContext(),
                R.layout.team_spinner_layout, TeamsList);

        teamASpin.setAdapter(adapter);
        teamBSpin.setAdapter(adapter2);

        teamASpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                teamA = teamsLits.get(position).toString();

                  //Toast.makeText(myView.getContext(),teamA,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamA = "Fc Barcelona";
            }

        });

        teamBSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                teamB = teamsLits.get(position).toString();

                //Toast.makeText(myView.getContext(),teamB,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                teamB = "Fc Barcelona";
            }

        });

        calculateCurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(teamA.equals(teamB))
                {
                    new AlertDialog.Builder(myView.getContext())
                            .setTitle(R.string.addMatch)
                            .setMessage(R.string.theSameTeamsError)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }

                            })
                            .show();
                }
                else
                {
                    setCurse();
                }
            }
        });

        return myView;

    }

    private void getTeam()
    {
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
                            int team_rating = jsonValues.get(i).getInt("team_rating");
                            int form_rating = jsonValues.get(i).getInt("form_rating");
                            double overall_rating = jsonValues.get(i).getDouble("overall_rating");

                            team = new Teams(id_team,name,team_rating,form_rating,overall_rating);
                            TeamsList.add(team);

                            teamsLits.add(name);

                            adapter.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();


                            teamsHashMap.put(name,id_team);

                        }

                        progressDialog.dismiss();

                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();


                    } else {

                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetTeamRequest gTR = new GetTeamRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(gTR);
    }

    private void setCurse()
    {

    }

}
