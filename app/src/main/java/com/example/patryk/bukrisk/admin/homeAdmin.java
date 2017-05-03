package com.example.patryk.bukrisk.admin;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.patryk.bukrisk.LoginRegister.LoginActivity;
import com.example.patryk.bukrisk.R;

public class homeAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        String mail = intent.getStringExtra("mail");

        TextView userName = (TextView)header.findViewById(R.id.adminName);
        TextView userEmail = (TextView)header.findViewById(R.id.adminEmail);

        userName.setText(name);
        userEmail.setText(mail);

        adminMain aM = new adminMain();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.content_home_admin,
                        aM,
                        aM.getTag()
                ).commit();
    }

    @Override
    public void onBackPressed() {

        confirmExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_admin, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.showMatches) {

        } else if (id == R.id.addNewMatch) {


            addMatch aT = new addMatch();

            FragmentManager manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.content_home_admin,
                            aT,
                            aT.getTag()
                    ).commit();

        } else if (id == R.id.addTeam) {

            addTeam aT = new addTeam();

            FragmentManager manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.content_home_admin,
                            aT,
                            aT.getTag()
                    ).commit();

        } else if (id == R.id.acceptPayments) {

            acceptPayments aP = new acceptPayments();

            FragmentManager manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.content_home_admin,
                            aP,
                            aP.getTag()
                    ).commit();

        } else if (id == R.id.logOutAdmin) {

            confirmLogout();


        } else if (id == R.id.exitAdmin) {

            confirmExit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmExit()
    {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(homeAdmin.this);

        builder.setTitle(R.string.confirmExit);

        builder.setMessage(R.string.confirmExitText)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final ProgressDialog progressDialog = new ProgressDialog(homeAdmin.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Exiting...");
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();

                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                },2000);

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

    private void confirmLogout()
    {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(homeAdmin.this);

        builder.setTitle(R.string.confirmLogout);

        builder.setMessage(R.string.logoutText)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final ProgressDialog progressDialog = new ProgressDialog(homeAdmin.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Logout ...");
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
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

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
