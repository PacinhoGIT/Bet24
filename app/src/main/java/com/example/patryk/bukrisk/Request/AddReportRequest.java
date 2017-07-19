package com.example.patryk.bukrisk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patryk on 2017-05-16.
 */

public class AddReportRequest  extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pacinho.000webhostapp.com/AddReport.php";
    private Map<String, String> params;

    public AddReportRequest(String id_user, String title, String text, String date, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("title", title);
        params.put("text", text);
        params.put("date", date);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
