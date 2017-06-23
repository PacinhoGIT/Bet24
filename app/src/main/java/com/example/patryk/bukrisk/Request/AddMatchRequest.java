package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-05-02.
 */

public class AddMatchRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/addMatch.php";
    private Map<String, String> params;

    public AddMatchRequest(String id_home, String id_away, String date, String score, String home, String draw, String away, String result, String key_match,Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_home", id_home);
        params.put("id_away", id_away);
        params.put("date", date);
        params.put("score", score);
        params.put("teamA", home);
        params.put("draw", draw);
        params.put("teamB", away);
        params.put("result", result);
        params.put("key_match", key_match);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
