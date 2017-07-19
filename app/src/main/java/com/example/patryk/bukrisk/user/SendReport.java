package com.example.patryk.bukrisk.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddBetsRequest;
import com.example.patryk.bukrisk.Request.AddReportRequest;
import com.example.patryk.bukrisk.adapter.Bets;
import com.example.patryk.bukrisk.adapter.Coupons;
import com.example.patryk.bukrisk.adapter.ShowCouponsCustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by patryk on 2017-07-19.
 */

public class SendReport extends Fragment {

    private ProgressDialog progressDialog;
    private EditText titleED, messageED;
    private Button send;

    private int year;
    private int month;
    private int day;
    String month1;
    String day1;

    String date;

    private int id_user;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.send_report_layout, container, false);

        titleED = (EditText) myView.findViewById(R.id.titleReportED);
        messageED = (EditText) myView.findViewById(R.id.textReportED);
        send= (Button) myView.findViewById(R.id.sendReportButton);

        Bundle bundle = getArguments();
        id_user = bundle.getInt("id_user");

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleS = titleED.getText().toString();
                String messageS = messageED.getText().toString();

                if(titleS.length()!=0 && messageS.length()!=0){
                    sendReport(""+id_user,titleS,messageS,date);
                }
                else{
                    Toast.makeText(myView.getContext(), "Title and Message can't be empty ! Correct and try agin. ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return myView;
    }

    private void sendReport(String id, final String title, String message, String date){

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
                        Toast.makeText(myView.getContext(), "Report send !", Toast.LENGTH_SHORT).show();

                        titleED.setText("");
                        messageED.setText("");

                    } else {
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    progressDialog.dismiss();
                    Toast.makeText(myView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };



        AddReportRequest addReport = new AddReportRequest(id,title,message,date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addReport);
    }
}
