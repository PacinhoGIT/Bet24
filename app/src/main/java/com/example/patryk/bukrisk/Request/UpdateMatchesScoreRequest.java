package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-13.
 */

public class UpdateMatchesScoreRequest  extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/UpdateMatchesScore.php";
    private Map<String, String> params;

    public UpdateMatchesScoreRequest(String id_match,String score, String result,  Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("score", score);
        params.put("result", result );
        params.put("id_match", id_match );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}