package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-08-04.
 */

public class GetReportsRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetReports.php";
    private Map<String, String> params;

    public GetReportsRequest(Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
