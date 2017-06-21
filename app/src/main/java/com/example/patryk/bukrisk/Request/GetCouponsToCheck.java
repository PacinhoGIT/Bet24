package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-15.
 */

public class GetCouponsToCheck extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetCouponsToCheck.php";
    private Map<String, String> params;

    public GetCouponsToCheck(String id_user, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
