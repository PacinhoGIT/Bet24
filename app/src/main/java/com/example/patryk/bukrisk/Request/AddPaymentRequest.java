package com.example.patryk.bukrisk.Request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-27.
 */

public class AddPaymentRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/Payments.php";
    private Map<String, String> params;

    public AddPaymentRequest(String id_user, String value, String accept, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("value", value);
        params.put("accept", accept);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
