package com.sportspartner.request;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.sportspartner.util.LoginDBHelper;
import com.sportspartner.util.NetworkResponseRequest;
import com.sportspartner.util.VolleyCallback;


/**
 * Created by xc on 10/21/17.
 */

public class UserRequest extends com.sportspartner.request.Request{

    public UserRequest(Context c){
        super(c);
    }

    public void loginVolleyRequest(final VolleyCallback callback, String email, String password) {
        JsonObject jsonRequestObject = new JsonObject();

        jsonRequestObject.addProperty("userId", email.trim().toLowerCase());
        jsonRequestObject.addProperty("password", password);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/login";

        NetworkResponseRequest nrRequest = new NetworkResponseRequest(Request.Method.POST, url, jsonRequestObject.toString(),
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

    public void logOutVolleyRequest(final VolleyCallback callback) {
        LoginDBHelper dbHelper = LoginDBHelper.getInstance(contextf);
        String userId = dbHelper.getEmail();
        String key = dbHelper.getKey();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/login?userId="+userId+"&key="+key;

        NetworkResponseRequest nrRequest = new NetworkResponseRequest(Request.Method.DELETE, url, null,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Context context = contextf.getApplicationContext();
//                Toast toast = Toast.makeText(context, "volley error: "+error.getMessage(), Toast.LENGTH_LONG);
//                toast.show();
            }
        }
        );
        queue.add(nrRequest);
    }

    public void signUpVolleyRequest(final VolleyCallback callback, String email, String password, String confirmPassword, String type) {

        JsonObject jsonRequestObject = new JsonObject();

        jsonRequestObject.addProperty("userId", email.trim().toLowerCase());
        jsonRequestObject.addProperty("password", password);
        jsonRequestObject.addProperty("confirmPassword", confirmPassword);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(contextf);
        String url = URL_CONTEXT+"v1/signup/"+type;

        NetworkResponseRequest nrRequest = new NetworkResponseRequest(Request.Method.POST, url, jsonRequestObject.toString(),
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
