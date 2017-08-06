package com.example.patryk.bukrisk.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetPaymentsHistoryRequest;
import com.example.patryk.bukrisk.Request.GetPaymentsRequest;
import com.example.patryk.bukrisk.adapter.Payments;
import com.example.patryk.bukrisk.adapter.PaymentsHistoryCustomAdapter;
import com.example.patryk.bukrisk.adapter.Reply;
import com.example.patryk.bukrisk.adapter.ReplyListCustomAdapter;
import com.example.patryk.bukrisk.adapter.Reports;
import com.example.patryk.bukrisk.adapter.UserReportsCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-08-06.
 */

public class PaymentsHistory extends Fragment {

    private ProgressDialog progressDialog;
    ListView paymentsLV;
    ArrayList<Payments> paymentses;
    PaymentsHistoryCustomAdapter paymentsHistoryCustomAdapter;

    private int id_user;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.users_reports_layout, container, false);
        paymentsLV = (ListView) myView.findViewById(R.id.usersReportLV);
        paymentses= new ArrayList<>();

        paymentsHistoryCustomAdapter = new PaymentsHistoryCustomAdapter(myView.getContext(),paymentses);
        paymentsLV.setAdapter(paymentsHistoryCustomAdapter);
        Payments p = new Payments("","","","Money","Accept","Date");
        paymentses.add(p);
        paymentsHistoryCustomAdapter.notifyDataSetChanged();

        Bundle bundle = getArguments();
        id_user = bundle.getInt("id_user");

        getPayments();

        return myView;
    }

    private void getPayments(){


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

                        JSONArray jsonArray = jsonResponse.getJSONArray("payment");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_pay = jsonValues.get(i).getInt("id_pay");
                            int id_user = jsonValues.get(i).getInt("id_user");
                            int value = jsonValues.get(i).getInt("value");
                            int accept = jsonValues.get(i).getInt("accept");
                            String date = jsonValues.get(i).getString("date");

                            Payments payments = new Payments(""+id_pay,""+id_user,"",""+value,""+accept,date);
                            paymentses.add(payments);
                            paymentsHistoryCustomAdapter.notifyDataSetChanged();

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

        GetPaymentsHistoryRequest paymentsRequest = new GetPaymentsHistoryRequest(""+id_user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(paymentsRequest);
    }
}
