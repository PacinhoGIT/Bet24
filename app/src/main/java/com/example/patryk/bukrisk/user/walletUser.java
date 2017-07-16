package com.example.patryk.bukrisk.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddPaymentRequest;
import com.example.patryk.bukrisk.Request.GetWalletRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patryk on 2017-04-27.
 */

public class walletUser extends Fragment {

    int value;

    String nameU;
    double valueS;
    int id_user;

    TextView funds;
    ProgressDialog progressDialog;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wallet_user_layout, container, false);

        TextView myWallet = (TextView) myView.findViewById(R.id.myWallet);
        TextView yourFunds = (TextView) myView.findViewById(R.id.yourFunds);
        funds = (TextView) myView.findViewById(R.id.funds);
        TextView addFunds = (TextView) myView.findViewById(R.id.addFunds);
        final TextView fundsTV = (TextView) myView.findViewById(R.id.fundsTV);
        final SeekBar fundsSeekBar = (SeekBar) myView.findViewById(R.id.fundsSeekBar);
        Button addFundsBTN = (Button) myView.findViewById(R.id.addFundsBTN);

        Bundle bundle = getArguments();
        nameU = bundle.getString("nameUser");
        id_user = bundle.getInt("id_user");

        myWallet.setVisibility(View.INVISIBLE);
        getWallet(nameU);

        addFundsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              addFundsToWallet();

            }
        });
        fundsTV.setText("5 PLN");

        fundsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if(progress<5)
                {
                    progress=5;
                }

                fundsTV.setText(String.valueOf(progress) + " PLN");
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return myView;

    }

    private void getWallet(String nameU)
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



                        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");

                         valueS = jsonResponse.getDouble("wallet");
                         String moneyS= String.valueOf(valueS);
                         moneyS = df.format(valueS);
                         moneyS = moneyS.replace(".",",");

                         funds.setText(""+moneyS + " PLN");

                        progressDialog.dismiss();


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

    private void addFundsToWallet()
    {
        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait !");
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
                            .setTitle(R.string.addFundsText)
                            .setMessage(R.string.succes)
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

    String fundsValue = String.valueOf(value);
    String idUser = String.valueOf(id_user);

    AddPaymentRequest addFunds = new AddPaymentRequest(idUser,fundsValue,"0", responseListener);
    RequestQueue queue = Volley.newRequestQueue(myView.getContext());
    queue.add(addFunds);
}
}
