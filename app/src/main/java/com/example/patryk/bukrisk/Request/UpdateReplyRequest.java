package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-08-06.
 */

public class UpdateReplyRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/UpdateReply.php";
    private Map<String, String> params;

    public UpdateReplyRequest(String Id_reply, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Id_reply", Id_reply );


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}