package com.example.q.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import Contact.Person;

import static java.security.AccessController.getContext;

/**
 * AsyncTask를 이용하여 REST POST콜을 통해 JSON을 입력하는 클래스.
 */
public class PostPerson {

    private Person person;
    private Context context;
    private  String memid;

    PostPerson(Context context) {
        this.context = context;
    }


    public void postPerson(Person person, String memid) {
        String url="http://socrip3.kaist.ac.kr:5580/api/contact/add";
        this.person = person;
        this.memid = memid;
        Log.d("dddd",memid);
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("id", memid);
            jObject.put("name", person.getName());
            jObject.put("number", person.getNumber());
        }catch(Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }});

        Volley.newRequestQueue(context).add(jsonRequest);
//        new PostTask().execute("http://socrip3.kaist.ac.kr:5580/api/contact/a");




    }

    // AsyncTask를 inner class로 구현
//    private class PostTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            Log.d("REST POST", params[0]);
//            try {
//                return POST(params[0]);
//            } catch (IOException e) {
//                return "Unable to retreive data. URL may be invalid.";
//            }
//        }
//    }

//    private String POST(String myurl) throws IOException {
//        InputStream inputStream = null;
//        String returnString = "";
//
//        JSONObject json = new JSONObject();
//
//        int length = 30000;
//
//        try {
//            URL url = new URL(myurl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.connect();
//            json.accumulate("name", person.getName());
//            Log.d("name",person.getNumber());
//            json.accumulate("Number", person.getNumber());
//            json.accumulate("id", person.getId());
//            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//            writer.write(json.toString());
//            writer.flush();
//            writer.close();
//
//            int response = conn.getResponseCode();
//            Log.d("REST POST", "The response is : " + response);
//        } catch (Exception e) {
//            Log.e("REST POST", "Error : " + e.getMessage());
//        } finally {
//            if (inputStream != null)
//                inputStream.close();
//        }
//        return returnString;
//    }
}
