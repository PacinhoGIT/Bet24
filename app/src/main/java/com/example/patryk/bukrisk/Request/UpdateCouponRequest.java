package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-08.
 */

public class UpdateCouponRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/updateCoupon.php";
    private Map<String, String> params;

    public UpdateCouponRequest(String total_course,String risk,String to_win, String id_coupon, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("total_course", total_course);
        params.put("risk", risk );
        params.put("to_win", to_win );
        params.put("id_coupon", id_coupon );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}