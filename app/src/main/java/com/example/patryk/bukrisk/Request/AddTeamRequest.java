package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-28.
 */

public class AddTeamRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/AddTeam.php";
    private Map<String, String> params;

    public AddTeamRequest(String name, String team_rating, String form_rating, String overall_rating,String logo,String liga, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("team_rating", team_rating );
        params.put("form_rating", form_rating);
        params.put("overall_rating", overall_rating );
        params.put("logo", logo );
        params.put("liga", liga );

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
