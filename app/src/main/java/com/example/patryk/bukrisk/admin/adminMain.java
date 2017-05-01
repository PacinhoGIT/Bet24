package com.example.patryk.bukrisk.admin;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

/**
 * Created by patryk on 2017-04-27.
 */

public class adminMain extends Fragment{

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.admin_home_layout, container, false);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("name");
        String mail = intent.getStringExtra("mail");

       TextView tv1 = (TextView) myView.findViewById(R.id.adminTV);
        tv1.setText("Zalogowany jako : " + name + " " + mail);


        return myView;
    }
}
