package com.sportspartner.request;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.sportspartner.util.NetworkResponseRequest;
import com.sportspartner.util.VolleyCallback;

/**
 * @author Xiaochen Li
 */

public class FacilityRequest extends Request {

    /**
     * Constructor
     * @param c Caller context
     */
    public FacilityRequest(Context c){
        super(c);
    }

    /**
     * Send a request for a list of all facilities.
     * @param callback Caller context.
     */
    public void allFacilitiesRequest(final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/facilities";

        NetworkResponseRequest nrRequest = new NetworkResponseRequest(com.android.volley.Request.Method.GET, url, null,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = contextf.getApplicationContext();
                Toast toast = Toast.makeText(context, "volley error: "+error.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        );
        queue.add(nrRequest);
    }
}
