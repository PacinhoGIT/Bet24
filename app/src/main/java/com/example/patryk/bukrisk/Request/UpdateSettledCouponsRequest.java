package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-25.
 */

public class UpdateSettledCouponsRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/UpdateSettledCoupons.php";
    private Map<String, String> params;

    public UpdateSettledCouponsRequest(String id_coupon,String settled, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_coupon", id_coupon);
        params.put("paid_off", settled );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}