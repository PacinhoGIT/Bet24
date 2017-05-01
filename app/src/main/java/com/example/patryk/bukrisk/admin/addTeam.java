package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddTeamRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by patryk on 2017-04-28.
 */

public class addTeam extends Fragment {

    View myView;
    TextView addTeam, teamName, teamRating, formRating, overallRating, overallValue, teamRatingValueTV,formRatingValueTV ;
    EditText teamNameED;
    Button addTeamBTN;
    SeekBar teamRatingValue, formRatingValue;

    double teamRatingVal;
    double formRatingVal;
    double overallRatingVall;

    String teamName1;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.add_team_layout, container, false);

        addTeam = (TextView) myView.findViewById(R.id.addNewTeamTV);
        teamName = (TextView) myView.findViewById(R.id.teamNameTV);
        teamRating = (TextView) myView.findViewById(R.id.teamRatingTV);
        formRating = (TextView) myView.findViewById(R.id.formRatingTV);
        overallRating = (TextView) myView.findViewById(R.id.overallRatingTV);
        overallValue = (TextView) myView.findViewById(R.id.overallRatingValue);
        teamRatingValueTV = (TextView) myView.findViewById(R.id.teamValueTV);
        formRatingValueTV = (TextView) myView.findViewById(R.id.formValueTV);

        teamNameED = (EditText) myView.findViewById(R.id.teamNameED);

        addTeamBTN = (Button) myView.findViewById(R.id.addTeamBTN);

        teamRatingValue = (SeekBar) myView.findViewById(R.id.teamRatingSB);
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
                overallValue.setText(""+overallRatingVall);
            }
        });

        formRatingValue = (SeekBar) myView.findViewById(R.id.formRatingSB);
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

                overallValue.setText(""+overallRatingVall);
            }
        });

        addTeamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teamName1 = teamNameED.getText().toString();

                addNewTeam();

            }
        });


        return myView;
    }

    private void addNewTeam()
    {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        new AlertDialog.Builder(myView.getContext())
                                .setTitle(R.string.addTeamText)
                                .setMessage(R.string.succesAddTeam)
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

        AddTeamRequest addteam1 = new AddTeamRequest(teamName1,""+teamRatingVal,""+formRatingVal,""+overallRatingVall, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addteam1);
    }
}
