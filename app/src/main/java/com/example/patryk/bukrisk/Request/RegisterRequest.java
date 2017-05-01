package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-27.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String pass, String mail, String adm, String wallet, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("pass", pass );
        params.put("mail", mail);
        params.put("adm", adm );
        params.put("wallet", wallet );

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
