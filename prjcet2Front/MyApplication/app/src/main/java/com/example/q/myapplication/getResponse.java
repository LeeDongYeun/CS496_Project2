package com.example.q.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2018-01-01.
 */

public class getResponse  extends AsyncTask<String, String,String>{
    Context context;
    String memid;
    ArrayList<String> photoList;

    public getResponse(Context context, String memid, ArrayList<String> photoList){
        this.context = context;
        this.memid = memid;
        this.photoList = photoList;
    }



    @Override
    protected String doInBackground(String... strings) {
        getPhotoArray(memid, photoList);
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        Log.d("result", result);
    }

    public void getPhotoArray(String memid, final ArrayList<String> photoList) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://socrip3.kaist.ac.kr:5580/api/photo/" + memid;
        Log.d("aaa", url);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("aaa", "fragment2");
                        try {
                            JSONArray contacts = response;
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject contact = contacts.getJSONObject(i);
                                String fileString = contact.getString("fileString");
                                Log.d("aaa", fileString);
                                photoList.add(fileString);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //photoList.add("endOfInput");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("aaa", error.toString());
                        //photoList.add("endOfInput");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

        //photoList.remove(photoList.size()-1);
        //Log.d("aaa", photoList.toString());
    }
}
