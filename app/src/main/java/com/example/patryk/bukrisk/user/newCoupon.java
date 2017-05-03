package com.example.patryk.bukrisk.user;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.patryk.bukrisk.R;

/**
 * Created by patryk on 2017-05-03.
 */

public class newCoupon extends Fragment {

    ProgressDialog progressDialog;

    String nameU;
    int id_user;

    View myView;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.new_coupon_layout, container, false);

        Bundle bundle = getArguments();
        nameU = bundle.getString("nameUser");
        id_user = bundle.getInt("id_user");

        return myView;
    }

}
