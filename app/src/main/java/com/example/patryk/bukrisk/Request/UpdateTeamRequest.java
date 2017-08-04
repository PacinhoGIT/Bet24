package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-07-26.
 */

public class UpdateTeamRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/UpdateTeam.php";
    private Map<String, String> params;

    public UpdateTeamRequest(String id, String name,String teamRat, String formRat,String overRat,String logo, String liga, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_team", id);
        params.put("name", name );
        params.put("team_rating", teamRat );
        params.put("form_rating", formRat );
        params.put("overall_rating", overRat );
        params.put("logo", logo );
        params.put("liga", liga );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}