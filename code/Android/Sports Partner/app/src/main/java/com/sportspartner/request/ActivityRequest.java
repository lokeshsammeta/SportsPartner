package com.sportspartner.request;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sportspartner.models.SActivity;
import com.sportspartner.util.LoginDBHelper;
import com.sportspartner.util.NetworkResponseRequest;
import com.sportspartner.util.VolleyCallback;


/**
 * Created by xc on 10/23/17.
 */

public class ActivityRequest extends com.sportspartner.request.Request{
    public ActivityRequest(Context c){
        super(c);
    }

    public void activitiesOutlineRequest(final VolleyCallback callback, String type, String id, int limit, int offset) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/activity_"+type+"?id="+id+"&offset="+offset+"&limit="+limit;
        //System.out.println(url);

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

    /*
    * @param type: either "full" for full activity or "outline" for activity outline
    * id: the id for this activity
    * */
    public void activityInfoRequest(final VolleyCallback callback, String type, String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/activity/"+id+"?content="+type;

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

    public void createActivityRequest(final VolleyCallback callback, SActivity activity) {


        LoginDBHelper db = LoginDBHelper.getInstance(contextf);
        String userEmail = db.getEmail();
        String key = db.getKey();

        Gson gson = new Gson();
        String activityString = gson.toJson(activity);
        JsonObject activityJsObj = gson.fromJson(activityString, JsonObject.class);

        JsonObject jsonRequestObject = new JsonObject();

        jsonRequestObject.addProperty("userId", userEmail.trim().toLowerCase());
        jsonRequestObject.addProperty("key", key);
        jsonRequestObject.add("activity", activityJsObj);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/activity";

        NetworkResponseRequest nrRequest = new NetworkResponseRequest(com.android.volley.Request.Method.POST, url,
                jsonRequestObject.toString(),
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
