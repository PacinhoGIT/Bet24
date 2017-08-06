package com.example.patryk.bukrisk.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-06-15.
 */

public class GetReplyRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL2 = "https://pacinho.000webhostapp.com/GetReplies.php";
    private Map<String, String> params;

    public GetReplyRequest(String id_report, Response.Listener<String> listener) {

        super(Request.Method.POST, LOGIN_REQUEST_URL2, listener, null);
        params = new HashMap<>();
        params.put("id_report", id_report);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
