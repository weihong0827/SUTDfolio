package com.example.sutdfolio.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class APIRequest {
    private static final String TAG = "API Request";
    private static final String prefixURL = "https://sutd-root-backend-w5e7n.ondigitalocean.app/";
    private NetworkManager netWorkInstance = NetworkManager.getInstance();
    private static APIRequest instance = null;
    private APIRequest(){
    };
    public static synchronized APIRequest getInstance(){
        if (instance == null){
            instance = new APIRequest();
        }
        return instance;
    }
    public void getUser (final Listener<String>listener,String jwt){
        String url = prefixURL + "api/user";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Login " + ": " + response.toString());
                        //TODO: Store the user data
                        if (null != response.toString())
                            listener.getResult(response.toString());
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                }
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-code",jwt);
                return headers;
            }
        };
        netWorkInstance.requestQueue.add(request);
    }
    public void verify(final Listener<String>listener,int otp,String detail,String email){
        String url = prefixURL + "api/login";
        JSONObject body = new JSONObject();
        try {
            body.put("otp",otp);
            body.put("verification_key",detail);
            body.put("check",email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Login " + ": " + response.toString());
                        //TODO: Store the JWT token that comes back in the system for future use
                        if (null != response.toString())
                            listener.getResult(response.toString());
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                }
            }
        });
        netWorkInstance.requestQueue.add(request);
    }
    public void login(final Listener<String>listener,String email,String password){
        String url = prefixURL + "api/login";
        JSONObject body = new JSONObject();
        try {
            body.put("email",email);
            body.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG + ": ", "Login " + ": " + response.toString());
                //TODO: retain the detail and use that for otp verification
                if (null != response.toString())
                    listener.getResult(response.toString());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                }
            }
        });
        netWorkInstance.requestQueue.add(request);
    }

    public void uploadImage(final Listener<String>listener,String image) {
        String url = prefixURL + "api/file";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG + ": ", "Image Uploaded " + ": " + response.toString());
                        if (null != response.toString())
                            //TODO:Load the response into the image object using gson
                            listener.getResult(response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                params.put("file", image);
                //returning parameters
                return params;
            }


        };
        netWorkInstance.requestQueue.add(request);
    }

    public void uploadPost(final Listener<String>listener,JSONObject postData){
        String url = prefixURL + "api/posts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG + ": ", "uploaded post "+": " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);

                        }
                    }
                });

        netWorkInstance.requestQueue.add(request);
    }
    public void getPost(final Listener<String>listener,String id){
        String url = prefixURL + "api/posts/"+id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG + ": ", "post "+id+": " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);

                        }
                    }
                });

        netWorkInstance.requestQueue.add(request);
    }

    public void getPosts( final Listener<String> listener)
    {
        String url = prefixURL + "api/posts";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        if(null != response.toString())
                            Log.d(TAG + ": ", "All posts : " + response.toString());
                            listener.getResult(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);

                        }
                    }
                });

        netWorkInstance.requestQueue.add(request);
    }

}
