package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddReplyRequest;
import com.example.patryk.bukrisk.Request.AddReportRequest;
import com.example.patryk.bukrisk.Request.GetReportsRequest;
import com.example.patryk.bukrisk.Request.UpdateReportRequest;
import com.example.patryk.bukrisk.Request.updateWalletRequest;
import com.example.patryk.bukrisk.adapter.Reports;
import com.example.patryk.bukrisk.adapter.ReportsCustomAdapter;
import com.example.patryk.bukrisk.adapter.ShowBetsInCouponCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by patryk on 2017-08-04.
 */

public class UsersReports extends Fragment{

    View myView;

    ListView reports;
    ProgressDialog progressDialog;
    ReportsCustomAdapter reportsCustomAdapter;
    Boolean showRaportTextBool=false;

    private int year;
    private int month;
    private int day;
    String month1;
    String day1;

    String date;

    ArrayList<Reports> reportses;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.users_reports_layout, container, false);
        reports = (ListView) myView.findViewById(R.id.usersReportLV);

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
        } else {
            day1 = "" + day;
        }


        date = year + "-" + month1 + "-" + day1;

        reportses = new ArrayList<>();

        reportsCustomAdapter = new ReportsCustomAdapter(myView.getContext(), reportses);
        reports.setAdapter(reportsCustomAdapter);

        reports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(reportses.get(position).getChecked().equals("F")) {
                    UpdateReport(reportses.get(position).getId_report());

                }
                showDetail(position);
            }

        });

        getReports(true);

        return myView;

    }

    private void getReports(final Boolean first) {

        if(first) {
            progressDialog = new ProgressDialog(myView.getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching data from the Server.");
            progressDialog.show();
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        reportses.clear();
                        JSONArray jsonArray = jsonResponse.getJSONArray("reports");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_report = jsonValues.get(i).getInt("id_report");
                            int id_user = jsonValues.get(i).getInt("id_user");
                            String title = jsonValues.get(i).getString("title");
                            String userName = jsonValues.get(i).getString("name");
                            String text = jsonValues.get(i).getString("text");
                            String date = jsonValues.get(i).getString("date");
                            String checked = jsonValues.get(i).getString("checked");

                            Reports r = new Reports(id_report,id_user,userName,title,text,date,checked);
                            reportses.add(r);
                            reportsCustomAdapter.notifyDataSetChanged();

                            if(first) {
                                Toast.makeText(myView.getContext(), "Succes !", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();


                        }

                    } else {

                        progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetReportsRequest gTR = new GetReportsRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(gTR);
    }
    private void UpdateReport(int id_report) {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait a moment ... ");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        getReports(false);

                    } else {

                        Toast.makeText(myView.getContext(),"Error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(myView.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        UpdateReportRequest uW = new UpdateReportRequest(""+id_report, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);
    }

    private void showDetail(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle("Raport detail");
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.raport_detail_layout, (ViewGroup) getView(), false);


        final TextView user = (TextView) viewInflated.findViewById(R.id.detailUser);
        final TextView date = (TextView) viewInflated.findViewById(R.id.detailDate);
        final TextView title = (TextView) viewInflated.findViewById(R.id.detailTitle);
        final TextView text = (TextView) viewInflated.findViewById(R.id.detailText);

        user.setText("Author : " + reportses.get(position).getUserName());
        date.setText("Date " + reportses.get(position).getDate());
        title.setText(reportses.get(position).getTitle());
        text.setText(reportses.get(position).getText());

        builder.setView(viewInflated)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        builder.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reply(reportses.get(position).getId_report(), position);
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void reply(final int id_report, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle("Reply to raport : " + id_report);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.write_reply_layout, (ViewGroup) getView(), false);



        final TextView reportText = (TextView) viewInflated.findViewById(R.id.raportTextReply);
        final Button showRaportText = (Button) viewInflated.findViewById(R.id.showTextReply);
        final EditText replyText = (EditText) viewInflated.findViewById(R.id.textReplyED);


        reportText.setVisibility(View.INVISIBLE);

        showRaportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRaportTextBool=!showRaportTextBool;

                if(showRaportTextBool){
                    reportText.setText(reportses.get(position).getText());
                    showRaportText.setText("Hide report content");
                    reportText.setVisibility(View.VISIBLE);
                }
                else{
                    reportText.setText("");
                    showRaportText.setText("Show report content");
                    reportText.setVisibility(View.INVISIBLE);
                }
            }
        });
        builder.setView(viewInflated)
                .setNegativeButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String text = replyText.getText().toString();
                addReply(id_report,text);
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();

    }


    private void addReply(int id_report, String text) {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending ... ");
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        progressDialog.dismiss();
                        Toast.makeText(myView.getContext(), "Reply send !", Toast.LENGTH_SHORT).show();



                    } else {
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    progressDialog.dismiss();
                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };



        AddReplyRequest addReport = new AddReplyRequest(""+id_report,text,"F",date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addReport);
    }

}
