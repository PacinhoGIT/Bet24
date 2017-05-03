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
import com.example.patryk.bukrisk.Request.LoginRequest;
import com.example.patryk.bukrisk.admin.homeAdmin;
import com.example.patryk.bukrisk.user.homeUser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView tvLogin, tvPassword, tvSingUp;
    EditText edLogin, edPassword;
    Button loginBtn;
    ImageView logo;

    boolean nameGood=false;
    boolean passGood=false;

    String password;

    private ProgressDialog pDialog;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        logo = (ImageView)findViewById(R.id.imageView);
        tvLogin = (TextView) findViewById(R.id.adminEmail);
        tvPassword = (TextView) findViewById(R.id.textView2);
        tvSingUp = (TextView) findViewById(R.id.singUp);

        edLogin =(EditText)findViewById(R.id.editLogin);
        edPassword =(EditText)findViewById(R.id.editPass);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        edLogin.setText(name);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        loginBtn = (Button) findViewById(R.id.loginButton);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if((edLogin.getText().toString()).equals("1"))
                {
                    Intent intent;
                    intent = new Intent(LoginActivity.this, homeAdmin.class);
                    intent.putExtra("name", "TestADM");
                    intent.putExtra("mail", "testADM@test.pl");

                    startActivity(intent);
                    finish();

                    // Test login to Admin accocunt without check login and password
                }
                else
                {
                    login();
                }

            }
        });


        tvSingUp.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();

            }
        });




    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setTitle(R.string.confirmExit);

        builder.setMessage(R.string.confirmExitText)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Exiting...");
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();
                                        finish();
                                    }
                                },2000);

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

    private void login()
    {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login...");
        progressDialog.show();

       valid();

        if (!valid())
        {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {


                            progressDialog.dismiss();
                            loginFailed();
                            if(nameGood==false)
                            {
                                edLogin.setError("Incorrect Login format ! Login must be over 1 characters !");
                            }
                            else{}
                            if(passGood==false)
                            {
                                edPassword.setError("Password must be over 5 characters !");
                            }
                            else{}

                            return;


                        }
                    }, 2000);

        }
        else
       {
           if(isNetworkAvailable()==true) {
               checkLogin();
           }
           else
           {

               new android.os.Handler().postDelayed(
                       new Runnable() {
                           public void run() {

                               progressDialog.dismiss();
                               Toast.makeText(getBaseContext(), "Error ! No internet connection !", Toast.LENGTH_LONG).show();
                           }
                       }, 500);


           }
       }
    }


    public void loginFailed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Login Failed. Invaalid login or password !")
                .setNegativeButton(R.string.retryBtn,
                  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                login();
            }
                  }
                );
        builder.setNeutralButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        progressDialog.dismiss();
                    }
                }
        );
        builder.create().show();


    }

    public boolean valid() {

        boolean valid = true;

        String login = edLogin.getText().toString();
        password = edPassword.getText().toString();

        if (login.length()<1)
        {
           // edLogin.setError("Incorrect E-mail format !");
            valid = false;
            nameGood=false;
        }
        else
        {
            nameGood=true;
        }

        if (password.isEmpty() || password.length() < 5)
        {
            //edPassword.setError("Password must be over 5 characters !");
            valid = false;
            passGood=false;
        }
        else
        {
            passGood=true;
        }

        return valid;
    }

    public void checkLogin()
    {

        final String nameU = edLogin.getText().toString();
        final String passU = edPassword.getText().toString();



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        String name = jsonResponse.getString("name");
                        String mail = jsonResponse.getString("mail");
                        int id_user = jsonResponse.getInt("id_user");
                        String adm = jsonResponse.getString("adm");

                        //Toast.makeText(getApplicationContext(),""+id_user,Toast.LENGTH_SHORT).show();
                        final Intent intent;

                        if(adm.equals("T"))
                        {

                            intent = new Intent(LoginActivity.this, homeAdmin.class);
                            intent.putExtra("name", name);
                            intent.putExtra("mail", mail);

                        }
                        else {


                            intent = new Intent(LoginActivity.this, homeUser.class);
                            intent.putExtra("name", name);
                            intent.putExtra("mail", mail);
                            intent.putExtra("id_user", id_user);
                            //finishAffinity();

                        }


                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        startActivity(intent);
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        Toast.makeText(getBaseContext(), R.string.loginSuccesText, Toast.LENGTH_LONG).show();

                                        // onLoginFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 2000);
                    } else {
                      loginFailed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(nameU, passU, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

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
