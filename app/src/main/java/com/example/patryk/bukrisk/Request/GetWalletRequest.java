package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-27.
 */

public class GetWalletRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://pacinho.000webhostapp.com/Wallet.php";
    private Map<String, String> params;

    public GetWalletRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}