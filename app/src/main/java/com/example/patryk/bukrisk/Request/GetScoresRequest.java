package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-15.
 */

public class GetScoresRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL2 = "https://pacinho.000webhostapp.com/GetScores.php";
    private Map<String, String> params;

    public GetScoresRequest( Response.Listener<String> listener) {

        super(Request.Method.POST, LOGIN_REQUEST_URL2, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
