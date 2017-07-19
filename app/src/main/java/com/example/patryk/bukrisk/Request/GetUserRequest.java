package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-07-12.
 */

public class GetUserRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/GetUser.php";
    private Map<String, String> params;

    public GetUserRequest(String name, String mail, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("mail", mail);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
