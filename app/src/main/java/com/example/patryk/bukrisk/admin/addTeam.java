package com.example.patryk.bukrisk.admin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.patryk.bukrisk.R;
import com.example.patryk.bukrisk.Request.AddTeamRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patryk on 2017-04-28.
 */

public class addTeam extends Fragment {

    View myView;
    TextView addTeam, teamName, teamRating, formRating, overallRating, overallValue, teamRatingValueTV,formRatingValueTV ;
    EditText teamNameED;
    Button addTeamBTN,logoBtn;
    SeekBar teamRatingValue, formRatingValue;

    double teamRatingVal;
    double formRatingVal;
    double overallRatingVall;

    String teamName1;

    ProgressDialog progressDialog;

    private int PICK_IMAGE_REQUEST = 1;

    String logo;
    ImageView  imageView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.add_team_layout, container, false);

        imageView = (ImageView) myView.findViewById(R.id.imageView2);

        teamName = (TextView) myView.findViewById(R.id.teamNameTV);
        teamRating = (TextView) myView.findViewById(R.id.teamRatingTV);
        formRating = (TextView) myView.findViewById(R.id.formRatingTV);
        overallRating = (TextView) myView.findViewById(R.id.overallRatingTV);
        overallValue = (TextView) myView.findViewById(R.id.overallRatingValue);
        teamRatingValueTV = (TextView) myView.findViewById(R.id.teamValueTV);
        formRatingValueTV = (TextView) myView.findViewById(R.id.formValueTV);

        teamNameED = (EditText) myView.findViewById(R.id.teamNameED);

        addTeamBTN = (Button) myView.findViewById(R.id.addTeamBTN);
        logoBtn = (Button) myView.findViewById(R.id.logoBtn);

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



        return myView;
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

                //imageView = (ImageView) myView.findViewById(R.id.imageView2);
                imageView.setVisibility(View.VISIBLE);
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

    private void addNewTeam()
    {

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding ...");
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
                                .setTitle(R.string.addTeamText)
                                .setMessage(R.string.succesAddTeam)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        teamNameED.setText("");
                                        teamRatingValue.setProgress(1);
                                        formRatingValue.setProgress(1);
                                        logo = "";
                                        overallValue.setText("1");
                                        formRatingValueTV.setText("1");
                                        teamRatingValueTV.setText("1");
                                        imageView.setVisibility(View.INVISIBLE);

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

        AddTeamRequest addteam1 = new AddTeamRequest(teamName1,""+teamRatingVal,""+formRatingVal,""+overallRatingVall,logo, responseListener);
        RequestQueue queue = Volley.newRequestQueue(myView.getContext());
        queue.add(addteam1);
    }
}
