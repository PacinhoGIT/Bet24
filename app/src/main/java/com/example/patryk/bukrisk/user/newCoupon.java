package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.patryk.bukrisk.LoginRegister.LoginActivity;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddBetsRequest;
import com.example.patryk.bukrisk.Request.AddCouponRequest;
import com.example.patryk.bukrisk.Request.AddMatchRequest;
import com.example.patryk.bukrisk.Request.AddPaymentRequest;
import com.example.patryk.bukrisk.Request.GetCouponIDRequest;
import com.example.patryk.bukrisk.Request.GetMatchesRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.Request.GetWalletRequest;
import com.example.patryk.bukrisk.Request.LoginRequest;
import com.example.patryk.bukrisk.Request.UpdateCouponRequest;
import com.example.patryk.bukrisk.Request.updatePaymentsRequest;
import com.example.patryk.bukrisk.Request.updateWalletRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Matches;
import com.example.patryk.bukrisk.adapter.NewCouponMatchesCustomAdapter;
import com.example.patryk.bukrisk.adapter.Payments;
import com.example.patryk.bukrisk.adapter.ShowBetsInCouponCustomAdapter;
import com.example.patryk.bukrisk.adapter.ShowMatchesCustomAdapter;
import com.example.patryk.bukrisk.admin.homeAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by patryk on 2017-05-03.
 */

public class newCoupon extends Fragment {

    ProgressDialog progressDialog;


    String nameU;
    String date;
    int id_user;
    int id_coupon;
    double money;
    double valueS;

    String type="A";

    private int year;
    private int month;
    private int day;
    String month1;
    String day1;

    HashMap<Integer, String> teamHash;
    HashMap<String, Integer> matchHash;

    String[] updateCouponArgs = new String[4];

    Matches match;

    ArrayList<Matches> matches;
    ArrayList<Bets> bets;

    NewCouponMatchesCustomAdapter matchesCustomAdapter;
    ShowBetsInCouponCustomAdapter betsCustomAdapter;

    TextView newCoupon,nameOfCoupon,currentMacthes,moneyTV;
    EditText nameOfCouponET;
    EditText moneyValue;
    ListView matchesLV;

    Button addBtn,save,betsInCoupon;


    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.new_coupon_layout, container, false);

        newCoupon = (TextView) myView.findViewById(R.id.newCouponTV);
        moneyTV = (TextView) myView.findViewById(R.id.moneyTV);
        nameOfCoupon = (TextView) myView.findViewById(R.id.nameOfCoupon);
        moneyValue = (EditText) myView.findViewById(R.id.moneyET);
        currentMacthes = (TextView) myView.findViewById(R.id.currentMatchesTV);
        betsInCoupon = (Button) myView.findViewById(R.id.showBetsInCouponBTN);
        save = (Button) myView.findViewById(R.id.saveCouponBTN);


        nameOfCouponET = (EditText) myView.findViewById(R.id.nameOfCouponET);

        matchesLV = (ListView) myView.findViewById(R.id.currentMatchesLV);
        matchesLV.setEnabled(false);

        matchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                Matches m = matches.get(position);

                String id = m.getId().toString();
                String nameM = m.getMatch().toString();
                String dateM = m.getDate().toString();
                String homeM = m.getA().toString();
                String drawM = m.getX().toString();
                String awayM = m.getB().toString();

                //Toast.makeText(myView.getContext(),nameM + dateM + homeM + drawM + awayM ,Toast.LENGTH_SHORT).show();
                showMatchDetail(id,nameM,dateM,homeM,drawM,awayM);
            }
        });


        addBtn = (Button) myView.findViewById(R.id.addCouponBtn);

        matches = new ArrayList<>();
        bets = new ArrayList<>();

        Bets b = new Bets("","Match","","Type","Course/Risk","");
        bets.add(b);

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

        betsCustomAdapter = new ShowBetsInCouponCustomAdapter(myView.getContext(),bets);


        Bundle bundle = getArguments();
        nameU = bundle.getString("nameUser");
        id_user = bundle.getInt("id_user");

        getTeam();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameOfCouponET.getText().toString()!=null || moneyValue.getText().toString()!=null ) {

                    try{
                        money=Double.parseDouble(moneyValue.getText().toString());
                        if(valueS>money) {
                            addCoupon();
                        }
                        else
                        {
                            Toast.makeText(myView.getContext(), "You don't have enough money !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e1)
                    {
                        Toast.makeText(myView.getContext(), "Incorrect money value !", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(myView.getContext(), "Name or Money can't be empty !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        betsInCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBetsInCoupon();
            }
        });

        betsInCoupon.setEnabled(false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBets();

            }
        });

        save.setEnabled(false);
        return myView;
    }

    private void updateCourse(String[] args)
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        progressDialog.dismiss();
                        new AlertDialog.Builder(myView.getContext())
                                .setTitle(R.string.addBets)
                                .setMessage(R.string.success)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Bundle args = new Bundle();
                                        args.putString("nameUser", nameU);
                                        args.putInt("id_user", id_user);

                                        newCoupon wU = new newCoupon();
                                        wU.setArguments(args);

                                        FragmentManager manager = getFragmentManager();
                                        manager.beginTransaction()
                                                .replace(R.id.content_home_user,
                                                        wU,
                                                        wU.getTag()
                                                ).commit();

                                        dialog.dismiss();
                                    }

                                })
                                .show();



                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UpdateCouponRequest uW = new UpdateCouponRequest("" + args[0], "" + args[1],""+args[2],""+args[3], responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);

    }

    private void showBetsInCoupon()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.betInCoupon);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.show_bets_in_coupon_alert_dialog, (ViewGroup) getView(), false);

        ListView betsLV = (ListView) viewInflated.findViewById(R.id.betsInCouponLV);
        betsCustomAdapter = new ShowBetsInCouponCustomAdapter(myView.getContext(),bets);
        betsLV.setAdapter(betsCustomAdapter);
        betsCustomAdapter.notifyDataSetChanged();

        final TextView total = (TextView) viewInflated.findViewById(R.id.totalTV);


         Double totalRiskValue = 0.0;
         Double totalCourseValue = 0.0;
         Double toWinValue = 0.0;


        if(bets.size()>0) {
            for (int i = 1; i < bets.size(); i++) {
                Double cValue = Double.valueOf(bets.get(i).getCourse().replace(",","."));
                totalCourseValue += cValue;

                Double rValue = Double.valueOf(bets.get(i).getRisk().replace(",","."));
                totalRiskValue += rValue;
            }

            toWinValue = (totalCourseValue * money) * 0.8;


        }
        java.text.DecimalFormat df=new java.text.DecimalFormat("0.00");


        total.setText(" Total Risk : " + df.format(totalRiskValue) + " %\n Total Course : " + df.format(totalCourseValue) + "\n To Win : " + df.format(toWinValue) + " PLN");

        updateCouponArgs = new String[4];

        updateCouponArgs[0]=""+df.format(totalCourseValue).replace(",",".");
        updateCouponArgs[1]=""+df.format(totalRiskValue).replace(",",".");
        updateCouponArgs[2]=""+df.format(toWinValue).replace(",",".");
        updateCouponArgs[3]=""+id_coupon;

        betsLV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
            {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(myView.getContext());

                builder.setTitle(R.string.deleteBet);

                builder.setMessage(R.string.confirmDeleteBet)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                bets.remove(position);
                                betsCustomAdapter.notifyDataSetChanged();

                                Double totalRiskValue = 0.0;
                                Double totalCourseValue = 0.0;
                                Double toWinValue = 0.0;

                                if(bets.size()>0) {
                                    for (int i = 1; i < bets.size(); i++) {



                                        Double cValue = Double.valueOf(bets.get(i).getCourse().replace(",","."));
                                        totalCourseValue += cValue;

                                        Double rValue = Double.valueOf(bets.get(i).getRisk().replace(",","."));
                                        totalRiskValue += rValue;
                                    }

                                    toWinValue = (totalCourseValue * money) * 0.8;


                                }
                                java.text.DecimalFormat df=new java.text.DecimalFormat("0.00");


                                total.setText(" Total Risk : " + df.format(totalRiskValue) + " %\n Total Course : " + df.format(totalCourseValue) + "\n To Win : " + df.format(toWinValue) + " PLN");

                                updateCouponArgs = new String[4];

                                updateCouponArgs[0]=""+df.format(totalCourseValue);
                                updateCouponArgs[1]=""+df.format(totalRiskValue);
                                updateCouponArgs[2]=""+df.format(toWinValue);
                                updateCouponArgs[3]=""+id_coupon;

                                if(bets.size()==1)
                                {
                                    save.setEnabled(false);
                                }


                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                android.support.v7.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        builder.setView(viewInflated)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }});

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void showMatchDetail(final String id,final String name, String date, final String home, final String draw, final String away)
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

        RadioGroup radioGroup = (RadioGroup)viewInflated.findViewById(R.id.typeRBG);
        //teamARB.setSelected(true);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (teamARB.isChecked()) {

                    type = "A";
                }
                if (drawRB.isChecked()) {

                    type = "X";

                }
                if (teamBRB.isChecked()) {

                    type = "B";
                }

            }
        });



        builder.setView(viewInflated)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        double a,x,b;
                        double userType=0;

                        a = Double.valueOf(home);
                        x = Double.valueOf(draw);
                        b = Double.valueOf(away);

                        if(type.equals("A"))
                        {
                            userType=a;

                        }
                        else if(type.equals("X"))
                        {
                            userType = x;
                        }
                        else if(type.equals("B"))
                        {
                            userType = b;
                        }

                        Double risk = (userType*100) / (a+b+x);

                        String id_match = id;
                        java.text.DecimalFormat df=new java.text.DecimalFormat("0.00");

                        String riskS = ""+ df.format(risk);

                        int temp=0;
                        int temp2=0;

                        if(bets.size()>1)
                        {
                            save.setEnabled(true);
                        }

                        if(bets.size()==1)
                        {
                            Bets bet = new Bets(""+id_match,name,""+id_coupon,type,""+userType,""+riskS);

                            bets.add(bet);
                            betsCustomAdapter.notifyDataSetChanged();
                            succesAddBetAlert();

                            save.setEnabled(true);

                            temp2++;


                        }
                        else if(bets.size()!=0 && bets.size()!=1){

                            for (int i = 1; i < bets.size(); i++) {

                               // Toast.makeText(myView.getContext(),"Bets id : " + bets.get(i).getId_match() + "select id " + id_match,Toast.LENGTH_SHORT).show();
                                if ((bets.get(i).getId_match()).equals(id_match)) {
                                   temp++;
                                }

                                }

                            }

                            if(temp>0)
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
                                alertDialog.setTitle(R.string.addBets);
                                alertDialog.setMessage("Failed ! This match is already added ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }

                            if(temp==0 && bets.size()!=1 && temp2==0) {

                                Bets bet = new Bets("" + id_match, name, "" + id_coupon, type, "" + userType, "" + df.format(risk));

                                bets.add(bet);
                                betsCustomAdapter.notifyDataSetChanged();

                                succesAddBetAlert();
                                save.setEnabled(true);
                            }





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

    private void succesAddBetAlert()
    {
        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(myView.getContext());

                        builder.setTitle(R.string.addBets);

                        builder.setMessage(R.string.success)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                    }
                                });

                        android.support.v7.app.AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        progressDialog.dismiss();


                    }
                },500);
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



                        //Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();



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

                            match = new Matches(""+idMatch,matchName,""+teamA,""+draw,""+teamB,data);

                            matches.add(match);


                            //showPay.setText(showPay.getText() + " Id_pay "+id_pay + " Id_user " + id_user + "Value " + value+" \n");
                        }

                       getWallet();


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

        final String name = nameOfCouponET.getText().toString();

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

                     getCouponID(name);

                    } else {}

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };



        AddCouponRequest addCoupon = new AddCouponRequest(""+id_user,""+name,""+money,date,"0","0","0", responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addCoupon);
    }

    private void getCouponID(String name)
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        id_coupon = jsonResponse.getInt("id_coupon");
                        matchesLV.setEnabled(true);
                        addBtn.setEnabled(false);
                        nameOfCouponET.setEnabled(false);
                        moneyValue.setEnabled(false);

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


                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetCouponIDRequest getCouponID = new GetCouponIDRequest(name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(getCouponID);
    }

    private void addBets()
    {
        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding ..." );
        progressDialog.show();


        for(int i=1;i<bets.size();i++) {

            final int i1=i;

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {

                        if(i1==bets.size()-1)
                        {
                          updateWallet();
                        }

                        } else {
                        }

                    } catch (JSONException e) {

                        Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            };

            //Bets bet = new Bets(""+id_match,name,""+id_coupon,type,""+userType,""+risk);

            Bets b = bets.get(i);
            String id_match = b.getId_match().toString();
            String id_coupon = b.getId_coupon().toString();
            String type = b.getType().toString();
            String course = b.getCourse().toString();
            String risk = b.getRisk().toString().replace(",",".");

            AddBetsRequest addBetss = new AddBetsRequest(id_match,id_coupon,type,course,risk, responseListener);
            RequestQueue queue = Volley.newRequestQueue(myView.getContext());
            queue.add(addBetss);
        }
    }

    private void getWallet()
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        valueS = jsonResponse.getDouble("wallet");
                        betsInCoupon.setEnabled(true);
                        matchesCustomAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();



                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        GetWalletRequest walletRequest = new GetWalletRequest(""+id_user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(walletRequest);
    }

    private void updateWallet(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        updateCourse(updateCouponArgs);


                    } else {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        updateWalletRequest uW = new updateWalletRequest("" + (valueS-money), "" + id_user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);
}


}
