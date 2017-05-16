package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-05-16.
 */

public class AddCouponRequest  extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/AddCoupon.php";
    private Map<String, String> params;

    public AddCouponRequest(String id_user, String name, String date, String total_course, String risk, String to_win, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("name", name);
        params.put("date", date);
        params.put("total_course", total_course);
        params.put("risk", risk);
        params.put("to_win", to_win);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
