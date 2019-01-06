package com.example.q.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class getResponse {
    static RequestQueue queue;
    static String result;
    String userId;
    String password;
    static String basicUrl = "http://socrip3.kaist.ac.kr:5580/api/";
    String additionalUrl;
    String type;
    Context context;

    /*
    public getResponse(Context context, String type, String additionalUrl, String id, String password){
        this.userId = id;
        this.password = password;
        this.additionalUrl = additionalUrl;
        this.type = type;
        this.context = context;
    }*/

    public static Integer loginOperation(Context context, String type, String additionalUrl, String id, String password){
        String url = basicUrl + additionalUrl + "/" + id + "/" + password;
        queue = Volley.newRequestQueue(context);

        Log.d("aaa", url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("aaa", "bbb");
                try {
                    result = response.getString("result");
                    //pw = response.getString("pw");
                    //language = response.getJSONArray("language");
                    //item = response.getJSONObject("item");
                    Log.d("aaa", result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("aaa", error.toString());
            }
        });

        jsonRequest.setTag("MAIN");
        queue.add(jsonRequest);

        return Integer.parseInt(result);
    }


}
