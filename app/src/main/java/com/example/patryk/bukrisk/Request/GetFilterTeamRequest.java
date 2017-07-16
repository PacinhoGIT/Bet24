package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetFilterTeamRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetFilterTeam.php";
    private Map<String, String> params;

    public GetFilterTeamRequest(String liga,Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("liga", liga);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
