package com.example.patryk.bukrisk.admin;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddTeamRequest;
import com.example.patryk.bukrisk.Request.GetFilterTeamRequest;
import com.example.patryk.bukrisk.Request.GetTeamRequest;
import com.example.patryk.bukrisk.Request.UpdateTeamRequest;
import com.example.patryk.bukrisk.adapter.ShowBetsInCouponCustomAdapter;
import com.example.patryk.bukrisk.adapter.Teams;
import com.example.patryk.bukrisk.adapter.TeamsCustomListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patryk on 2017-07-26.
 */

public class AllTeams extends Fragment {

    View myView;

    ListView teamsLV;
    Spinner liga;
    ArrayList<Teams> teamsList;
    ProgressDialog progressDialog;

    String logo;
    ImageView  imageView;



    double teamRatingVal;
    double formRatingVal;
    double overallRatingVall;
    String ligaName;

    Bitmap decodedByte;

    private int PICK_IMAGE_REQUEST = 1;

    TeamsCustomListAdapter teamsCustomListAdapter;

    final ArrayList<String> ligas = new ArrayList<String>();
    final HashMap<String, String> ligaHash = new HashMap<>();
    ArrayAdapter<String> dataAdapter;


    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.all_teams_layout, container, false);

        imageView = (ImageView) myView.findViewById(R.id.imageView2);
        teamsLV = (ListView) myView.findViewById(R.id.allTeamLV);
        liga = (Spinner) myView.findViewById(R.id.LigaSpinn);

        ligas.add("LFP");
        ligas.add("Bundesliga");
        ligas.add("Serie A");
        ligas.add("Ligue 1");
        ligas.add("BPL");
        ligas.add("Ekstraklasa");
        ligas.add("Other");

        ligaHash.put("LFP","LFP");
        ligaHash.put("Bundesliga","BL");
        ligaHash.put("Serie A","SA");
        ligaHash.put("Ligue 1","L1");
        ligaHash.put("BPL","BPL");
        ligaHash.put("Ekstraklasa","LE");
        ligaHash.put("Other","Other");


        dataAdapter = new ArrayAdapter<String>(myView.getContext(),
                android.R.layout.simple_spinner_item, ligas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        liga.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        liga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                String ligaFilter=ligaHash.get(ligas.get(position));
                getFilterTeam(ligaFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        teamsList = new ArrayList<>();
        teamsCustomListAdapter = new TeamsCustomListAdapter(myView.getContext(), teamsList);
        teamsLV.setAdapter(teamsCustomListAdapter);
       // getFilterTeam("LFP");

        teamsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                teamDetail(teamsList.get(position));
            }
        });

        return myView;

    }

    private void getFilterTeam(String liga){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait a second ...");
        progressDialog.show();



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    teamsList.clear();

                    if (success) {

                        teamsList.clear();

                        JSONArray jsonArray = jsonResponse.getJSONArray("teams");

                        ArrayList<JSONObject> jsonValues = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonValues.add(jsonArray.getJSONObject(i));

                            int id_team = jsonValues.get(i).getInt("id_team");
                            String name = jsonValues.get(i).getString("name");
                            int team_rating = jsonValues.get(i).getInt("team_rating");
                            int form_rating = jsonValues.get(i).getInt("form_rating");
                            double overall_rating = jsonValues.get(i).getDouble("overall_rating");
                            String logo = jsonValues.get(i).getString("logo");
                            String liga = jsonValues.get(i).getString("liga");

                            Teams team = new Teams(id_team,name,team_rating,form_rating,overall_rating,logo,liga);
                            teamsList.add(team);
                            teamsCustomListAdapter.notifyDataSetChanged();


                        }

                        progressDialog.dismiss();
                        if(teamsList.size()>0){
                            Toast.makeText(myView.getContext(),R.string.success, Toast.LENGTH_SHORT).show();}
                        else if(teamsList.size()==0){
                            Toast.makeText(myView.getContext(),"No Teams !", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        teamsList.clear();
                        teamsCustomListAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        };



        GetFilterTeamRequest GT = new GetFilterTeamRequest(liga, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(GT);

    }

    private void teamDetail(final Teams t){

        AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());
        builder.setTitle(R.string.betInCoupon);
        View viewInflated = LayoutInflater.from(myView.getContext()).inflate(R.layout.add_team_layout, (ViewGroup) getView(), false);

        final TextView  addTeam, teamName, teamRating, formRating, overallRating, overallValue, teamRatingValueTV,formRatingValueTV ;
        final EditText teamNameED;
        final Button logoBtn,add;
        final SeekBar teamRatingValue, formRatingValue;
        final Spinner ligaa;

        viewInflated.setBackgroundColor(Color.WHITE);
        imageView = (ImageView) viewInflated.findViewById(R.id.imageView2);


        add = (Button) viewInflated.findViewById(R.id.addTeamBTN);
        add.setVisibility(View.INVISIBLE);
        teamName = (TextView) viewInflated.findViewById(R.id.teamNameTV);
        teamRating = (TextView) viewInflated.findViewById(R.id.teamRatingTV);
        formRating = (TextView) viewInflated.findViewById(R.id.formRatingTV);
        teamRatingValueTV = (TextView) viewInflated.findViewById(R.id.teamValueTV);
        formRatingValueTV = (TextView) viewInflated.findViewById(R.id.formValueTV);
        teamNameED = (EditText) viewInflated.findViewById(R.id.teamNameED);
        logoBtn = (Button) viewInflated.findViewById(R.id.logoBtn);
        ligaa = (Spinner) viewInflated.findViewById(R.id.ligaSpinnerAddTeam);

        teamNameED.setEnabled(false);
        logoBtn.setEnabled(false);
        ligaa.setEnabled(false);

        teamNameED.setText(t.getName());

        teamRatingValue = (SeekBar) viewInflated.findViewById(R.id.teamRatingSB);
        teamRatingValue.setProgress(t.getTeam_rating());
        teamRatingValueTV.setText(""+t.getTeam_rating());
        teamRatingValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(progress<1)
                {
                    progress=1;
                }

                teamRatingValueTV.setText(String.valueOf(progress));
                teamRatingVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                overallRatingVall = (teamRatingVal + formRatingVal)/2;

            }
        });

        formRatingValue = (SeekBar) viewInflated.findViewById(R.id.formRatingSB);
        formRatingValue.setProgress(t.getForm_rating());
        formRatingValueTV.setText(""+t.getForm_rating());
        formRatingValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if(progress<1)
                {
                    progress=1;
                }

                formRatingValueTV.setText(String.valueOf(progress));
                formRatingVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                overallRatingVall = (teamRatingVal + formRatingVal)/2;


            }
        });

        teamRatingValue.setEnabled(false);
        formRatingValue.setEnabled(false);

        decodeByte64(t.getLogo());
        imageView.setImageBitmap(decodedByte);

        HashMap<String,String> ligaHash2 = new HashMap<>();
        ligaHash2.put("LFP","LFP");
        ligaHash2.put("BL","Bundesliga");
        ligaHash2.put("SA","Serie A");
        ligaHash2.put("L1","Ligue 1");
        ligaHash2.put("BPL","BPL");
        ligaHash2.put("LE","Ekstraklasa");
        ligaHash2.put("Other","Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myView.getContext(),
                android.R.layout.simple_spinner_item, ligas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ligaa.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        ligaa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                ligaName =ligaHash.get(ligas.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ligaName="LFP";
            }

        });

        int index = 0;

        for (int i = 0; i < liga.getCount(); i++) {
            if (liga.getItemAtPosition(i).toString().equalsIgnoreCase(ligaHash2.get(t.getLiga().toString()))) {
                index = i;
                break;
            }
        }
        ligaa.setSelection(index);

        logoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });



        builder.setView(viewInflated)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


        builder.setPositiveButton(R.string.edit, null);

        final android.app.AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                final Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        teamRatingValue.setEnabled(true);
                        formRatingValue.setEnabled(true);
                        teamNameED.setEnabled(true);
                        logoBtn.setEnabled(true);
                        ligaa.setEnabled(true);
                        add.setEnabled(true);
                        add.setVisibility(View.VISIBLE);
                        add.setText(R.string.save);

                        logo = t.getLogo().toString();
                        formRatingVal = t.getForm_rating();
                        teamRatingVal = t.getTeam_rating();
                        overallRatingVall = t.getOverall_rating();
                        b.setEnabled(false);


                    }
                });
            }
        });
        alert.show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Teams updateTeam = new Teams(t.getId_team(),teamNameED.getText().toString()
                ,(int)teamRatingVal,(int)formRatingVal,overallRatingVall,logo,ligaName);
                updateTeam(updateTeam, alert);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            }catch (Exception e){

                AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
                alertDialog.setTitle( "Select Image");
                alertDialog.setMessage("Error ! Select other image !");

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
            logo = encodeImage(bitmap);

            try {

                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {

                AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();
                alertDialog.setTitle("Select Image");
                alertDialog.setMessage("Error ! Select other image !");

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {

        String encodedString=null;

        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload
        // easy
        try {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } catch (Exception e1) {


        }


        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String

        try {
            encodedString = Base64.encodeToString(byte_arr, 0);
        } catch (Exception e) {

        }


        return encodedString;

    }
    private void decodeByte64(String code){

        byte[] decodedString = Base64.decode(code, Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

    private void updateTeam(final Teams t, final android.app.AlertDialog alert){

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating ...");
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
                                .setTitle(R.string.updateTeam)
                                .setMessage(R.string.updateTeamSucces)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        alert.dismiss();

                                        AllTeams aT = new AllTeams();

                                        FragmentManager manager = getFragmentManager();
                                        manager.beginTransaction()
                                                .replace(R.id.content_home_admin,
                                                        aT,
                                                        aT.getTag()
                                                ).commit();
                                    }

                                })
                                .show();
                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };

        UpdateTeamRequest addteam1 = new UpdateTeamRequest(""+t.getId_team(),t.getName(),""+t.getTeam_rating(),""+t.getForm_rating(),""+t.getOverall_rating(),t.getLogo(),t.getLiga() ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addteam1);

    }
}
