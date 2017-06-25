package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-25.
 */

public class GetBetsInCouponRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetBetsInCoupon.php";
    private Map<String, String> params;

    public GetBetsInCouponRequest(String id_coupon, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_coupon", id_coupon);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}