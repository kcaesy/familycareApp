package com.familycaretrust.familycaremicro;

/**
 * Created by kcaesy on 11/10/2017.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest{
    private static final String SEARCH_REQUEST_URL = "https://familycaretrust.com/admin/micro-api.php?action=getCustomer";
    private Map<String, String> params;

    public SearchRequest(String ussd, Response.Listener<String> listener) {
        super(Method.POST, SEARCH_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("ussd", ussd);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
