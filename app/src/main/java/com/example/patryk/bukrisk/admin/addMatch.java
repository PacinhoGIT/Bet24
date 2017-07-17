package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddMatchRequest;
import com.example.patryk.bukrisk.Request.AddTeamRequest;
import com.example.patryk.bukrisk.Request.GetFilterTeamRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.adapter.TeamSpinnerAdapter;
import com.example.patryk.bukrisk.adapter.Teams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patryk on 2017-04-30.
 */

public class addMatch extends Fragment {

    View myView;

    Button addBTN, calculateCurse,selectA,selectB;
    ImageButton dateBTN;
    ImageView logoA, logoB;
    Spinner teamASpin;
    Spinner teamBSpin;
    TextView dateTV,curseATV, curseDrawTV,curseBTV;

    Teams team;

    ArrayList<Teams> TeamsList;
    ArrayList<Teams> TeamFilterList;
    ArrayList<String> teamsLits;
    HashMap<String, Integer> teamsHashMap;

    HashMap<String, Double> overallRating;

    ProgressDialog progressDialog;

    private int year;
    private int month;
    private int day;

    String today;

    String month1;
    String day1;
    String teamA,teamB;

    String teamAA,teamBB;

    double A, D, B;

    TeamSpinnerAdapter adapter;
    TeamSpinnerAdapter adapter2;
    TeamSpinnerAdapter selectTeam;

    Boolean getDataSucces = false;

    Bitmap decodedByte;
    int pos;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.add_match_layout, container, false);

        addBTN = (Button) myView.findViewById(R.id.addMatchBTN);
        selectA = (Button) myView.findViewById(R.id.selectTeamAButton);
        selectB = (Button) myView.findViewById(R.id.selectTeamBButton);

        addBTN.setEnabled(false);

        dateBTN = (ImageButton) myView.findViewById(R.id.dateImgBtn);
        logoA = (ImageView) myView.findViewById(R.id.teamALogoAddMatchImageButton);
        logoB = (ImageView) myView.findViewById(R.id.teamBLogoAddMatchImageButton);

        calculateCurse = (Button) myView.findViewById(R.id.calculateCurseBTN);

        teamASpin = (Spinner) myView.findViewById(R.id.teamASpin);
        teamBSpin = (Spinner) myView.findViewById(R.id.teamBSpin);

        dateTV = (TextView) myView.findViewById(R.id.dateTV);
        curseATV = (TextView) myView.findViewById(R.id.curseATV);
        curseBTV = (TextView) myView.findViewById(R.id.curseBTV);
        curseDrawTV = (TextView) myView.findViewById(R.id.curseDrawTV);

        TeamsList = new ArrayList<Teams>();
        TeamFilterList = new ArrayList<Teams>();
        teamsLits = new ArrayList<String>();


        teamsHashMap = new HashMap<>();
        overallRating = new HashMap<>();

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

        today = year+"-"+month1+"-"+day1;

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

                                String monthS ;
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

      /*  adapter = new TeamSpinnerAdapter(myView.getContext(),
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

        */

        calculateCurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(teamA!=null && teamB!=null){

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

                        double curse[] = setCurse(overallRating.get(teamA),overallRating.get(teamB));

                        java.text.DecimalFormat df=new java.text.DecimalFormat("0.00");

                        A = curse[0];
                        D = curse[2];
                        B = curse[1];

                        curseATV.setText("" + df.format(curse[0]));
                        curseDrawTV.setText("" + df.format(curse[2]));
                        curseBTV.setText("" + df.format(curse[1]));

                        addBTN.setEnabled(true);
                        //calculateCurse.setEnabled(false);
                    }

                }else{

                    new AlertDialog.Builder(myView.getContext())
                            .setTitle(R.string.addMatch)
                            .setMessage("Select two diffrent teams !")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }

                            })
                            .show();

                }


            }
        });


        addBTN.setOnClickListener(new View.OnClickListener() {
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

                    addMatches();

                }
            }
        });

        selectA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelectTeamDialog("A");

            }
        });

        selectB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelectTeamDialog("B");

            }
        });




        return myView;

    }

    private void showSelectTeamDialog(final String teamAorB){

        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle("Select Team");
        final View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.select_team_custom_alert_dialog_layout, (ViewGroup) getView(), false);

        TextView liga = (TextView) viewInflated.findViewById(R.id.textView19);
        final TextView team = (TextView) viewInflated.findViewById(R.id.textView24);

        Spinner ligaSpin = (Spinner) viewInflated.findViewById(R.id.ligaSpinner);
        Spinner teamSpin = (Spinner) viewInflated.findViewById(R.id.teamSpinner);


        selectTeam = new TeamSpinnerAdapter(myView.getContext(),
                R.layout.team_spinner_layout, TeamFilterList);

        teamSpin.setAdapter(selectTeam);

        final ArrayList<String> ligas = new ArrayList<String>();
        ligas.add("LFP");
        ligas.add("Bundesliga");
        ligas.add("Serie A");
        ligas.add("Ligue 1");
        ligas.add("BPL");
        ligas.add("Ekstraklasa");
        ligas.add("Other");

        final HashMap<String, String> ligaHash = new HashMap<>();
        ligaHash.put("LFP","LFP");
        ligaHash.put("Bundesliga","BL");
        ligaHash.put("Serie A","SA");
        ligaHash.put("Ligue 1","L1");
        ligaHash.put("BPL","BPL");
        ligaHash.put("Ekstraklasa","LE");
        ligaHash.put("Other","Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(viewInflated.getContext(),
                android.R.layout.simple_spinner_item, ligas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ligaSpin.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        ligaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                String ligaFilter=ligaHash.get(ligas.get(position));
                getFilterTeam(ligaFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        teamSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pos=position;

                    if(teamAorB.equals("A")){

                        Teams t = TeamFilterList.get(position);
                        teamAA=t.getName();
                    }else if(teamAorB.equals("B")){

                        Teams t = TeamFilterList.get(position);
                        teamBB=t.getName();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(TeamFilterList.size()>0) {
                    if(teamAorB.equals("A")){
                    teamAA = TeamFilterList.get(pos).getName();
                    }
                    else if(teamAorB.equals("B")){
                        teamBB = TeamFilterList.get(pos).getName();
                    }
                }
            }

        });

        builder.setView(viewInflated)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(teamAorB.equals("A")){

                            Teams t = TeamFilterList.get(pos);
                            teamAA=t.getName();
                        }else if(teamAorB.equals("B")){

                            Teams t = TeamFilterList.get(pos);
                            teamBB=t.getName();
                        }

                        if(TeamFilterList.size()>0) {
                            Teams t = TeamFilterList.get(pos);
                            decodeByte64(t.getLogo());

                            if (teamAorB.equals("A")) {


                                logoA.setImageBitmap(decodedByte);
                                teamA = teamAA;

                            } else if (teamAorB.equals("B")) {

                                logoB.setImageBitmap(decodedByte);
                                teamB = teamBB;

                            }

                            curseATV.setText("");
                            curseDrawTV.setText("");
                            curseBTV.setText("");
                            addBTN.setEnabled(false);
                        }

                        dialog.dismiss();

                    }
                });

        builder.setView(viewInflated)
                .setNegativeButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                    }
                });


        android.app.AlertDialog alert = builder.create();
        alert.show();

    }

    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

    private void getFilterTeam(String liga){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait a second ...");
        progressDialog.show();



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    TeamFilterList.clear();

                    if (success) {

                        TeamFilterList.clear();

                        JSONArray jsonArray = jsonResponse.getJSONArray("teams");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_team = jsonValues.get(i).getInt("id_team");
                            String name = jsonValues.get(i).getString("name");
                            int team_rating = jsonValues.get(i).getInt("team_rating");
                            int form_rating = jsonValues.get(i).getInt("form_rating");
                            double overall_rating = jsonValues.get(i).getDouble("overall_rating");
                            String logo = jsonValues.get(i).getString("logo");

                            Teams team = new Teams(id_team,name,team_rating,form_rating,overall_rating,logo);
                            TeamFilterList.add(team);
                            selectTeam.notifyDataSetChanged();


                        }

                        progressDialog.dismiss();
                        if(TeamFilterList.size()>0){
                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();}
                        else if(TeamFilterList.size()==0){
                            Toast.makeText(myView.getContext(),"No Teams !", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        TeamFilterList.clear();
                        selectTeam.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        };



        GetFilterTeamRequest GT = new GetFilterTeamRequest(liga, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(GT);

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
                            String logo = jsonValues.get(i).getString("logo");

                            team = new Teams(id_team,name,team_rating,form_rating,overall_rating,logo);
                            TeamsList.add(team);

                            teamsLits.add(name);

                          /*  adapter.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();
                            */

                            teamsHashMap.put(name,id_team);
                            overallRating.put(name,overall_rating);

                        }

                        progressDialog.dismiss();

                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();

                        getDataSucces =  true;


                    } else {

                        progressDialog.dismiss();
                        getDataSucces =  false;

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

    private double[] setCurse(double a, double b) {

        double[] course = new double[3];

        double sub = Math.abs(a-b);

        double curseA=0;
        double curseB=0;
        double curseX=0;


        if(sub<=3)
        {
            if(a>b)
            {
                curseA = ThreadLocalRandom.current().nextDouble(3, 6);
                curseB = curseA+ThreadLocalRandom.current().nextDouble(0.5, 2);

            }
            else if(a<b)
            {
                curseB = ThreadLocalRandom.current().nextDouble(3, 6);
                curseA = curseB+ThreadLocalRandom.current().nextDouble(0.5, 2);
            }
            else
            {
                curseA = ThreadLocalRandom.current().nextDouble(3, 6);
                curseB = curseA+ThreadLocalRandom.current().nextDouble(0.5, 1);
            }

            curseX = ThreadLocalRandom.current().nextDouble(1.1, 2);
        }

        if(sub>3 && sub<=6)
        {
            if(a>b)
            {
                curseA = ThreadLocalRandom.current().nextDouble(1.1, 2);
                curseB = ThreadLocalRandom.current().nextDouble(6,7);

            }
            else if(a<b)
            {
                curseB = ThreadLocalRandom.current().nextDouble(1.1, 2);
                curseA = ThreadLocalRandom.current().nextDouble(6,7);
            }

            curseX = ThreadLocalRandom.current().nextDouble(4.01, 5);
        }

        if(sub>6)
        {
            if(a>b)
            {
                curseA = ThreadLocalRandom.current().nextDouble(1.1, 2);
                curseB = ThreadLocalRandom.current().nextDouble(8.5, 10);

            }
            else if(a<b)
            {
                curseB = ThreadLocalRandom.current().nextDouble(1.1, 2);
                curseA = ThreadLocalRandom.current().nextDouble(8.5, 10);
            }

            curseX = ThreadLocalRandom.current().nextDouble(4.5, 6);
        }

        course[0]=curseA;
        course[1]=curseB;
        course[2]=curseX;

        return course;
    }

    private void addMatches()
    {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding ...");
        progressDialog.show();


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        curseATV.setText("");
                        curseDrawTV.setText("");
                        curseBTV.setText("");

                        dateTV.setText(today);

                        teamASpin.setSelection(0);
                        teamBSpin.setSelection(0);

                        progressDialog.dismiss();


                        new AlertDialog.Builder(myView.getContext())
                                .setTitle(R.string.addMatch)
                                .setMessage(R.string.success)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }

                                })
                                .show();
                    } else {}

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };

        int idA = teamsHashMap.get(teamA);
        int idB = teamsHashMap.get(teamB);

        String AS = curseATV.getText().toString().replace(",",".");
        String DS = curseDrawTV.getText().toString().replace(",",".");
        String BS = curseBTV.getText().toString().replace(",",".");

        String key_match = "H"+idA+"A"+idB+"D"+dateTV.getText();

        AddMatchRequest addteam1 = new AddMatchRequest(""+idA,""+idB,dateTV.getText().toString(),"-",""+AS,""+DS,""+BS,"-",key_match, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addteam1);
    }

}
