package com.fcmtest.choiwp10.fcmtest.Util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;
public class HttpRequest {
    private static HttpRequest _instance;
    private RequestQueue _requestQueue;
    private static Context _context;

    public interface HttpResponseCallback {
        void onCallback(Boolean success, JSONObject jsonData);
    }

    public interface HttpResponseStringCallback {
        void onCallback(Boolean success, String stringData);
    }

    private HttpRequest(Context context) {
        _context = context;
        _requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequest getInstance(Context context) {
        if (_instance == null) {
            _instance = new HttpRequest(context);
        }
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if(_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(_context.getApplicationContext());
        }
        return  _requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void httpGet(String url, final HttpResponseCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response) {
                        Log.d("HttpRequest", response.toString());
                        callback.onCallback(true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("HttpRequest", error.getMessage());
                        callback.onCallback(false, null);
                    }
                }
        );
        addToRequestQueue(jsObjRequest);
    }


    public void httpGet(String url, final HttpResponseStringCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpRequest", response.toString());
                        callback.onCallback(true, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("HttpRequest", error.toString());
                        callback.onCallback(false, null);
                    }
                }
        );
        addToRequestQueue(stringRequest);
    }

    public void httpPost(String url, String jsonData, final HttpResponseCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onCallback(true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onCallback(false, null);
                    }
                });
        addToRequestQueue(jsonObjectRequest);
    }

    public void httpPost(String url, JSONObject postData, final HttpResponseCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onCallback(true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onCallback(false, null);
                    }
                });
        addToRequestQueue(jsonObjectRequest);
    }

    public void httpPost(String url, Map<String, Object> postData, final HttpResponseCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postData),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onCallback(true, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onCallback(false, null);
                    }
                });
        addToRequestQueue(jsonObjectRequest);
    }
}
