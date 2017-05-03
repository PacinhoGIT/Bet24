package com.example.patryk.bukrisk.LoginRegister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtNewUser,txtUserName,txtEmail,txtPassword,txtConfirmPassword;
    private EditText edUserName,edPassword,edConfirmPassword,edUserEmail;
    private Button btnRegister;
    private ImageView goodIconUE,goodIconUN,goodIconUP,goodIconUCP;

    String userName,userEmail,userPassword,userConfirmPassword;

    private ProgressDialog pDialog;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_screan);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtNewUser = (TextView) findViewById(R.id.txtNewUser);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtEmail = (TextView) findViewById(R.id.txtUserEmail);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtConfirmPassword = (TextView) findViewById(R.id.txtConfirmPassword);

        edUserName = (EditText) findViewById(R.id.edUserName);
        edUserEmail = (EditText) findViewById(R.id.edUserEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edConfirmPassword = (EditText) findViewById(R.id.edConfirmPassword);

        goodIconUE = (ImageView) findViewById(R.id.goodIconUE);
        goodIconUN = (ImageView) findViewById(R.id.goodIconUN);
        goodIconUP = (ImageView) findViewById(R.id.goodIconUP);
        goodIconUCP = (ImageView) findViewById(R.id.goodIconUCP);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        edUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 4) {
                    goodIconUN.setVisibility(View.VISIBLE);
                } else {
                    goodIconUN.setVisibility(View.INVISIBLE);
                }
            }

        });

        edUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(editable).matches()) {
                    goodIconUE.setVisibility(View.INVISIBLE);
                } else {
                    goodIconUE.setVisibility(View.VISIBLE);
                }
            }

        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty() || editable.length() < 5) {
                    goodIconUP.setVisibility(View.INVISIBLE);

                } else {
                    goodIconUP.setVisibility(View.VISIBLE);
                    userPassword = editable.toString();
                }
            }

        });

        edConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals(userPassword) && editable.toString().isEmpty()==false) {
                    goodIconUCP.setVisibility(View.VISIBLE);
                } else {
                    goodIconUCP.setVisibility(View.INVISIBLE);
                }
            }

        });


        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Register...");
                progressDialog.show();

                int goodDate = 0;

                userName = edUserName.getText().toString();
                userEmail = edUserEmail.getText().toString();
                userPassword = edPassword.getText().toString();
                userConfirmPassword = edConfirmPassword.getText().toString();


                if (userName.length() < 5 || userName.isEmpty()) {
                    edUserName.setError("User name mus be over 4 characters !");
                    goodIconUN.setVisibility(View.INVISIBLE);
                } else {
                    goodIconUN.setVisibility(View.VISIBLE);
                    goodDate++;
                }


                if (userEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    edUserEmail.setError("Incorrect E-mail format !");
                    goodIconUE.setVisibility(View.INVISIBLE);
                } else {
                    goodIconUE.setVisibility(View.VISIBLE);
                    goodDate++;
                }

                if (userPassword.isEmpty() || userPassword.length() < 5) {
                    edPassword.setError("Password must be over 4 characters !");
                    goodIconUP.setVisibility(View.INVISIBLE);

                } else {
                    goodIconUP.setVisibility(View.VISIBLE);
                    goodDate++;
                }

                if (userPassword.equals(userConfirmPassword) && userPassword != null) {
                    goodIconUCP.setVisibility(View.VISIBLE);
                    goodDate++;
                } else {
                    goodIconUCP.setVisibility(View.INVISIBLE);
                    edConfirmPassword.setError("Passwords must be the same !");
                }

                if (goodDate == 4) {

                    if (isNetworkAvailable() == true) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {



                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        intent.putExtra("name", userName);

                                                        RegisterActivity.this.startActivity(intent);
                                                        progressDialog.dismiss();
                                                          finishAffinity();


                                    } else {
                                        Toast.makeText(getBaseContext(), R.string.registerFailedText, Toast.LENGTH_LONG).show();


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        RegisterRequest registerRequest = new RegisterRequest(userName, userPassword, userEmail, "N", "100", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(registerRequest);
                    } else {



                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(), R.string.noInterentRegister, Toast.LENGTH_LONG).show();

                                    }
                                }, 1500);


                    }
                }
                else
                {

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {

                                    progressDialog.dismiss();
                                    Toast.makeText(getBaseContext(), R.string.registerFailedText, Toast.LENGTH_LONG).show();

                                }
                            }, 500);
                }
            }
            });


        }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setTitle(R.string.confirmExit);

        builder.setMessage(R.string.cancelAddNewUserText)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkAvailable()
    {
         ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = cm.getActiveNetworkInfo();

         if (networkInfo != null && networkInfo.isConnected())
         {
             return true;
         }

         else {
             return false;
         }

    }



}
