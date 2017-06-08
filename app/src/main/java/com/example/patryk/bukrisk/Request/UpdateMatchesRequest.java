package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-08.
 */

public class UpdateMatchesRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/updateMatches.php";
    private Map<String, String> params;

    public UpdateMatchesRequest(String teamA,String draw,String teamB, String id_match, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("teamA", teamA);
        params.put("draw", draw );
        params.put("teamB", teamB );
        params.put("id_match", id_match );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}