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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetReplyRequest;
import com.example.patryk.bukrisk.Request.GetReportsRequest;
import com.example.patryk.bukrisk.Request.GetUserReportRequest;
import com.example.patryk.bukrisk.Request.UpdateReplyRequest;
import com.example.patryk.bukrisk.Request.UpdateReportRequest;
import com.example.patryk.bukrisk.adapter.Reply;
import com.example.patryk.bukrisk.adapter.ReplyListCustomAdapter;
import com.example.patryk.bukrisk.adapter.Reports;
import com.example.patryk.bukrisk.adapter.ReportsCustomAdapter;
import com.example.patryk.bukrisk.adapter.UserReportsCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by patryk on 2017-08-05.
 */

public class MyReport extends Fragment {

    private ProgressDialog progressDialog;
    ListView reports;
    ArrayList<Reports> reportses;
    ArrayList<Reply> replyArrayList;

    UserReportsCustomAdapter adapter;
    ReplyListCustomAdapter replyListCustomAdapter;

    private int id_user;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.users_reports_layout, container, false);
        reports = (ListView) myView.findViewById(R.id.usersReportLV);

        reportses = new ArrayList<>();

        Reports r = new Reports(0,0,"","Title","","Date","Read");
        reportses.add(r);


        adapter = new UserReportsCustomAdapter(myView.getContext(), reportses);


        reports.setAdapter(adapter);

        Bundle bundle = getArguments();
        id_user = bundle.getInt("id_user");

        getReports();

        reports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showReplayAlert(position);

            }
        });

        return myView;
    }

    private void getReports() {


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

                        JSONArray jsonArray = jsonResponse.getJSONArray("reports");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_report = jsonValues.get(i).getInt("id_report");
                            int id_user = jsonValues.get(i).getInt("id_user");
                            String title = jsonValues.get(i).getString("title");
                            String text = jsonValues.get(i).getString("text");
                            String date = jsonValues.get(i).getString("date");
                            String checked = jsonValues.get(i).getString("checked");

                            Reports r = new Reports(id_report,id_user,"",title,text,date,checked);
                            reportses.add(r);
                            adapter.notifyDataSetChanged();


                              //  Toast.makeText(myView.getContext(), "Succes !", Toast.LENGTH_SHORT).show();

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

        GetUserReportRequest gTR = new GetUserReportRequest(""+id_user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(gTR);
    }

    private void showReplayAlert(int position){


        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle("Reply to raport " + reportses.get(position).getId_report());
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.set_matches_score_layout, (ViewGroup) getView(), false);

        replyArrayList = new ArrayList<>();
        replyListCustomAdapter = new ReplyListCustomAdapter(myView.getContext(), replyArrayList);
        ListView reply = (ListView) viewInflated.findViewById(R.id.set_score_lv);
        reply.setAdapter(replyListCustomAdapter);


        getReply(reportses.get(position).getId_report(), true);


        reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                showFullReplyText(pos);
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

    private void showFullReplyText(final int pos){

        AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
        alertDialog.setTitle("Reply message");
        alertDialog.setMessage(replyArrayList.get(pos).getText());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(replyArrayList.get(pos).getChecked().equals("F")) {
                            UpdateReply(replyArrayList.get(pos).getId_report(), replyArrayList.get(pos).getId_reply());
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private void getReply(int id, final Boolean first) {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        replyArrayList.clear();

                        JSONArray jsonArray = jsonResponse.getJSONArray("answers");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_reply = jsonValues.get(i).getInt("Id_reply");
                            int id_report = jsonValues.get(i).getInt("id_report");
                            String text = jsonValues.get(i).getString("text");
                            String date = jsonValues.get(i).getString("date");
                            String checked = jsonValues.get(i).getString("checked");

                            Reply r = new Reply(id_reply,id_report,text,checked,date);
                            replyArrayList.add(r);
                            replyListCustomAdapter.notifyDataSetChanged();

                            if(first) {
                                Toast.makeText(myView.getContext(), "Succes !", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();


                        }

                    } else {

                     //   progressDialog.dismiss();


                    }

                } catch (JSONException e) {

                    Toast.makeText(myView.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        GetReplyRequest gTR = new GetReplyRequest(""+id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(gTR);
    }

    private void UpdateReply(final int id_reports, int Id_reply) {

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


                        getReply(id_reports,false);

                    } else {

                        Toast.makeText(myView.getContext(),"Error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(myView.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        UpdateReplyRequest uW = new UpdateReplyRequest(""+Id_reply, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(uW);
    }


}