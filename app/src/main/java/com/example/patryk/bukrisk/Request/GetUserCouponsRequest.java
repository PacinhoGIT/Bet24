package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-23.
 */

public class GetUserCouponsRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetUserCoupons.php";
    private Map<String, String> params;

    public GetUserCouponsRequest(String id_user,Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
