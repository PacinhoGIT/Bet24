package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-04-30.
 */

public class updateWalletRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/updateWallet.php";
    private Map<String, String> params;

    public updateWalletRequest(String wallet, String id_user, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("wallet", wallet);
        params.put("id_user", id_user );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}