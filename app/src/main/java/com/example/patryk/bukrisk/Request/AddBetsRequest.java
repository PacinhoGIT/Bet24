package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-05-17.
 */

public class AddBetsRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/AddBets.php";
    private Map<String, String> params;

    public AddBetsRequest(String id_match, String id_coupon, String type, String course, String risk,String key_bet, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_match", id_match);
        params.put("id_coupon", id_coupon);
        params.put("type", type);
        params.put("course", course);
        params.put("risk", risk);
        params.put("key_bet", key_bet);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
