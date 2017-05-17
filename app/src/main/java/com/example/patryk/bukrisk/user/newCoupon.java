package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddCouponRequest;
import com.example.patryk.bukrisk.Request.AddMatchRequest;
import com.example.patryk.bukrisk.Request.GetMatchesRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.NewCouponMatchesCustomAdapter;
import com.example.patryk.bukrisk.adapter.ShowMatchesCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by patryk on 2017-05-03.
 */

public class newCoupon extends Fragment {

    ProgressDialog progressDialog;


    String nameU;
    String date;
    int id_user;

    private int year;
    private int month;
    private int day;
    String month1;
    String day1;

    HashMap<Integer, String> teamHash;
    HashMap<String, Integer> matchHash;

    Matches match;

    ArrayList<Matches> matches;

    NewCouponMatchesCustomAdapter matchesCustomAdapter;

    TextView newCoupon,nameOfCoupon,currentMacthes,betInCoupon;
    EditText nameOfCouponET;
    ListView matchesLV,betsLV;

    Button addBtn;


    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.new_coupon_layout, container, false);

        newCoupon = (TextView) myView.findViewById(R.id.newCouponTV);
        nameOfCoupon = (TextView) myView.findViewById(R.id.nameOfCoupon);
        currentMacthes = (TextView) myView.findViewById(R.id.currentMatchesTV);
        betInCoupon = (TextView) myView.findViewById(R.id.betInCouponTV);

        nameOfCouponET = (EditText) myView.findViewById(R.id.nameOfCouponET);

        matchesLV = (ListView) myView.findViewById(R.id.currentMatchesLV);
        matchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                Matches m = matches.get(position);

                String nameM = m.getMatch().toString();
                String dateM = m.getDate().toString();
                String homeM = m.getA().toString();
                String drawM = m.getX().toString();
                String awayM = m.getB().toString();

                //Toast.makeText(myView.getContext(),nameM + dateM + homeM + drawM + awayM ,Toast.LENGTH_SHORT).show();
                showMatchDetail(nameM,dateM,homeM,drawM,awayM); // nie dziala ustawianie tekstu !
            }
        });

        betsLV = (ListView) myView.findViewById(R.id.betInCouponLV);

        addBtn = (Button) myView.findViewById(R.id.addCouponBtn);

        matches = new ArrayList<>();
        teamHash = new HashMap<>();
        matchHash = new HashMap<>();

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


        date = year + "-" + month1 + "-" + day1;

        matchesCustomAdapter = new NewCouponMatchesCustomAdapter(myView.getContext(),matches);
        matchesLV.setAdapter(matchesCustomAdapter);

        String cars[] = {"Mercedes", "Fiat"};

        ArrayAdapter adapter = new ArrayAdapter(myView.getContext(), android.R.layout.simple_list_item_1, cars);
        betsLV.setAdapter(adapter);


        Bundle bundle = getArguments();
        nameU = bundle.getString("nameUser");
        id_user = bundle.getInt("id_user");

        getTeam();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameOfCouponET.getText().toString()!=null) {
                    addCoupon();
                }
                else
                {
                    Toast.makeText(myView.getContext(), "Name can't be empty !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return myView;
    }

    private void showMatchDetail(String name, String date, String home, String draw, String away)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.matchDetail);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.match_detail_alert_dialog_layout, (ViewGroup) getView(), false);

        final TextView nameTV = (TextView) viewInflated.findViewById(R.id.showNameDetail);
        final TextView dateTV = (TextView) viewInflated.findViewById(R.id.showDateDetail);

        final TextView teamATV = (TextView) viewInflated.findViewById(R.id.homeCourseDetailTV);
        final TextView drawTV = (TextView) viewInflated.findViewById(R.id.drawCourseDetailTV);
        final TextView teamBTV = (TextView) viewInflated.findViewById(R.id.awayCourseDetailTV);

        final RadioButton teamARB = (RadioButton)  viewInflated.findViewById(R.id.homeRadioBtn);
        final RadioButton drawRB = (RadioButton)  viewInflated.findViewById(R.id.drawRadioBtn);
        final RadioButton teamBRB = (RadioButton)  viewInflated.findViewById(R.id.awayRadioBtn);



            nameTV.setText("" + name);
            dateTV.setText("" + date);
            teamATV.setText("" + home);
            drawTV.setText("" + draw);
            teamBTV.setText("" + away);


        //nameTV.setText("DUPA !");


       RadioGroup radioGroup = (RadioGroup)viewInflated.findViewById(R.id.typeRBG);
        //teamARB.setSelected(true);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (teamARB.isChecked()) {


                }
                if (drawRB.isChecked()) {





                }
                if (teamBRB.isChecked()) {

                }

            }
        });



        builder.setView(viewInflated)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {




                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }});

        android.app.AlertDialog alert = builder.create();
        alert.show();
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

                            teamHash.put(id_team,name);

                        }

                        progressDialog.dismiss();

                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();



                        getMatches();


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

    private void getMatches()
    {

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

                            String matchName = teamAName + " - " + teamBName;
                            matchHash.put(matchName,idMatch);

                            match = new Matches(matchName,""+teamA,""+draw,""+teamB,data);

                            matches.add(match);
                            matchesCustomAdapter.notifyDataSetChanged();

                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
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

        GetMatchesRequest matchesRequest = new GetMatchesRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(matchesRequest);

    }

    private void addCoupon()
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

                        progressDialog.dismiss();

                        new AlertDialog.Builder(myView.getContext())
                                .setTitle(R.string.newCoupon)
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

        String name = nameOfCouponET.getText().toString();

        AddCouponRequest addCoupon = new AddCouponRequest(""+id_user,""+name,date,"0","0","0", responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addCoupon);
    }

}
