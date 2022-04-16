package com.example.sutdfolio.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sutdfolio.R;
import com.example.sutdfolio.data.LoginDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Map;

public class APIRequest {
    private static final String TAG = "API Request";
    private static final String prefixURL = "https://sutd-root-backend-w5e7n.ondigitalocean.app/";

    private NetworkManager netWorkInstance = NetworkManager.getInstance();
    static final float DEFAULT_BACKOFF_MULT = 1f;
    private static APIRequest instance = null;
    private APIRequest(){
    };
    public static synchronized APIRequest getInstance(){
        if (instance == null){
            instance = new APIRequest();
        }
        return instance;
    }
    public void getUser (final Listener<JSONObject>listener,String jwt, Activity activity){
        String url = prefixURL + "api/user";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "User " + ": " + response.toString());
                        //TODO: Store the user data
                        if (null != response)
                            listener.getResult(response);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                    Log.d(TAG + ": ", "Error Response message: " + error.getMessage());
                }
                Toast.makeText(activity.getApplicationContext(), R.string.error+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-token",jwt);
                return headers;
            }
        };
        netWorkInstance.requestQueue.add(request);
    }
    public void getCourse(final Listener<String> listener, Activity activity){
        String url = prefixURL + "api/posts/courses";
        Log.d(TAG, "getCourse: start"+url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d(TAG + ": ", "get courses" + response.toString());
                        if(null != response.toString())
                            listener.getResult(response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d(TAG, "onErrorResponse: "+error);
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
//                            listener.getResult(false);
                        }
                        Toast.makeText(activity.getApplicationContext(), R.string.error+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        netWorkInstance.requestQueue.add(request);
    }
    public void getTags(final Listener<String> listener, Activity activity){
        String url = prefixURL + "api/posts/tags";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d(TAG + ": ", "get courses" + response.toString());
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
                        Toast.makeText(activity.getApplicationContext(), R.string.error+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        netWorkInstance.requestQueue.add(request);
    }

    public void verify(final Listener<JSONObject>listener,int otp,String detail,String email, TextView errorText, TextView errorText2){
        String url = prefixURL + "api/user/verify/otp";
        JSONObject body = new JSONObject();
        try {
            body.put("otp",otp);
            body.put("verification_key",detail);
            body.put("check",email);
            errorText.setVisibility(View.INVISIBLE);
            errorText2.setVisibility(View.INVISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
            errorText.setVisibility(View.VISIBLE);
            errorText2.setVisibility(View.VISIBLE);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Login " + ": " + response.toString());
                        //TODO: Store the JWT token that comes back in the system for future use
                        if (null != response)
                            listener.getResult(response);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText2.setVisibility(View.VISIBLE);
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                    Log.d(TAG + ": ", "Error Message: " + error.getMessage());
                }
            }
        });
        netWorkInstance.requestQueue.add(request);
    }

    public void register(final Listener<JSONObject>listener,String email,String password,String name,int studentId, TextView errorText){
        String url = prefixURL + "api/user/register";

        JSONObject body = new JSONObject();
        try {
            body.put("email",email);
            body.put("password",password);
            body.put("name",name);
            body.put("studentId",studentId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG + ": ", "Register " + ": " + body.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Register " + ": " + response.toString());
                        if (null != response.toString())
                            listener.getResult(response);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorText.setVisibility(View.VISIBLE);
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                    Log.d(TAG + ": ", "Error Message: " + error.getMessage());
                }
            }
        });
        netWorkInstance.requestQueue.add(request);
    }

    public void editUser(final Listener<JSONObject>listener,String aboutMe,String pillar,String class_of,String avatar, String jwt, View view){
        String url = prefixURL + "api/user";

        JSONObject body = new JSONObject();
        try {
            body.put("aboutMe",aboutMe);
            body.put("pillar",pillar);
            body.put("class_of",class_of);
            body.put("avatar",avatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG + ": ", "Edit profile " + ": " + body.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Edit profile " + ": " + response.toString());
                        if (null != response.toString())
                            listener.getResult(response);
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                    Log.d(TAG + ": ", "Error Message: " + error.getMessage());
                }
                Toast.makeText(view.getContext(), R.string.error+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-token",jwt);
                return headers;
            }
        };
        netWorkInstance.requestQueue.add(request);
    }

    public void login(final Listener<JSONObject>listener, String email, String password,  Response.ErrorListener errorListener, View view){
        String url = prefixURL + "api/user/login";

        JSONObject body = new JSONObject();
        try {
            body.put("email",email);
            body.put("password",password);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(view.getContext(), R.string.error+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Log.d(TAG + ": ", "Login " + ": " + body.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG + ": ", "Login " + ": " + response.toString());
                //TODO: retain the detail and use that for otp verification
                if (null != response.toString())
                    listener.getResult(response);
            }
        },errorListener);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        netWorkInstance.requestQueue.add(request);
    }


    public void uploadPost(final Listener<JSONObject>listener, JSONObject postData,String jwt, View view){
        String url = prefixURL + "api/posts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG + ": ", "uploaded post "+": " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response);

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
                            Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
//                            listener.getResult(false);
                        }
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-token",jwt);
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }

    public void getPost(final Listener<String>listener,String id,String token, View view){
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
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                if (!token.isEmpty()) {
                    headers.put("auth-token", token);
                }
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }

    public void updatePost(final Listener<JSONObject>listener,String id,String token, JSONObject postData, View view){
        String url = prefixURL + "api/posts/" +id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, postData,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d(TAG + ": ", "post "+id+": " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response);

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
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                if (!token.isEmpty()) {
                    headers.put("auth-token", token);
                }
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }

    public void delPost(final Listener<String>listener,String id,String token, View view){
        String url = prefixURL + "api/posts/"+id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
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
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                if (!token.isEmpty()) {
                    headers.put("auth-token", token);
                }
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }

    public void getPosts( final Listener<String> listener,String token, View view)
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
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                if (!token.isEmpty()){
                    headers.put("auth-token",token);
                }
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }
    public void upVote(final Listener<String> listener, String id,String jwt, View view){
        String url = prefixURL + "api/posts/upvote/"+id;
        Log.d(TAG, "upVote: "+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        if(null != response.toString())
                            Log.d(TAG + ": ", "Upvote : " + response.toString());
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
                            Log.d(TAG + ": ", "Error Response message: " + error.getMessage());
//                            listener.getResult(false);
                        }
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-token",jwt);
                return headers;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }
    public void delImage(String filename,String jwt,String projectId, View view){
        String url = prefixURL + "api/file/"+filename;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        if(null != response.toString())
                            Log.d(TAG, "onResponse: Image deleted");

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
                            Log.d(TAG + ": ", "Error Response message: " + error.getMessage());
//                            listener.getResult(false);

                        }
                        Toast.makeText(view.getContext(), R.string.error+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("auth-token",jwt);
                return headers;
            }
            @Override
            public Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                if (!projectId.isEmpty()){
                    params.put("project",projectId);
                }
                return params;
            }
        };

        netWorkInstance.requestQueue.add(request);
    }
}
