package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-30.
 */

public class updatePaymentsRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/updatePayments.php";
    private Map<String, String> params;

    public updatePaymentsRequest(String id_pay, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_pay", id_pay );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}