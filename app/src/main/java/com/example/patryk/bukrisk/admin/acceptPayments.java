package com.example.patryk.bukrisk.admin;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.GetPaymentsRequest;
import com.example.patryk.bukrisk.adapter.AcceptPaymentsCustomAdapter;
import com.example.patryk.bukrisk.adapter.Payments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patryk on 2017-04-28.
 */

public class acceptPayments extends Fragment {



    View myView;
    TextView showPay;

    Payments payments;

    ArrayList<Payments> paymenstsList;
    AcceptPaymentsCustomAdapter paymentsCustomAdapter;

    ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.accept_payments_layout, container, false);

        TextView acceptPayText = (TextView) myView.findViewById(R.id.acceptPayText);
        ListView paymentss = (ListView) myView.findViewById(R.id.payLV);
        showPay = (TextView) myView.findViewById(R.id.showPay);

        paymenstsList = new ArrayList<Payments>();
        paymentsCustomAdapter = new AcceptPaymentsCustomAdapter(myView.getContext(),paymenstsList);

        paymentss.setAdapter(paymentsCustomAdapter);

        getPayments();

        return myView;
    }

    public void getPayments()
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

                        JSONArray jsonArray = jsonResponse.getJSONArray("payment");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_pay = jsonValues.get(i).getInt("id_pay");
                            int id_user = jsonValues.get(i).getInt("id_user");
                            int value = jsonValues.get(i).getInt("value");
                            int accept = jsonValues.get(i).getInt("accept");

                            payments = new Payments(id_pay,id_user,value,accept);
                            paymenstsList.add(payments);

                            paymentsCustomAdapter.notifyDataSetChanged();

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

        GetPaymentsRequest paymentsRequest = new GetPaymentsRequest("0", responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(paymentsRequest);

    }

}









