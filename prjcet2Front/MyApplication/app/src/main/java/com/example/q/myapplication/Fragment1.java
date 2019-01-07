package com.example.q.myapplication;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    ListView listView;
    CustomListAdapter adapter;
    ArrayList<list_item> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        this.listView = (ListView) view.findViewById(R.id.address);
        this.list = new ArrayList<list_item>();
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    String rawJSON = "{\n" +
//                            "\t\"attempts\": 5,\n" +
//                            "\t\"autorun\": false,\n" +
//                            "\t\"defaultExtension\": \".html\",\n" +
//                            "\t\"interval\": 500,\n" +
//                            "\t\"mimetypes\":\n" +
//                            "\t{\n" +
//                            "\t\t\"\": \"application/octet-stream\",\n" +
//                            "\t\t\".c\": \"text/plain\",\n" +
//                            "\t\t\".h\": \"text/plain\",\n" +
//                            "\t\t\".py\": \"text/plain\"\n" +
//                            "\t},\n" +
//                            "\t\"port\": 8080\n" +
//                            "}\n";

//                    JSONObject root = new JSONObject(rawJSON);
//                    JSONObject data = root.getJSONObject("data");
//                    Integer count = data.getInt("count");
//                    JSONArray contacts = data.getJSONArray("contacts");
//
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject contact = (JSONObject) contacts.get(i);
//                        list.add(new list_item(contact.getString("Name"), contact.getString("PhoneNumber")));
//                    }
//                }
//                catch {
//                    Log.d("Fragment1", "parsing failed");
//                }
//                list.add(new list_item("Lee", "12345"));
//                list.add(new list_item("abababa", "12413131345"));

                Log.d("Fragment1", "Clicked");
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                // Initialize a new JsonArrayRequest instance
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.POST,
                        "http://127.0.0.1:8080/test/test.json",
                        new JSONArray(),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Do something with response
                                //mTextView.setText(response.toString());
                                Log.d("Fragment1", "0");
                                // Process the JSON
                                try {
                                    JSONArray contacts = response;
                                    Log.d("Fragment1", "1");
                                    for (int i = 0; i < contacts.length(); i++) {
                                        JSONObject contact = contacts.getJSONObject(i);

                                        String name = contact.getString("Name");
                                        String phoneNumber = contact.getString("PhoneNumber");
//                                        if (checkExistence != 0) {
                                        list.add(new list_item(name, phoneNumber));
                                        Log.d("addwithExistence", "Yeah");
//
//                                        } else {
//                                            list.add(new list_item(nam));
//                                            //Log.d("addwithExistence", "Nooo");
//                                        }
                                        //Log.d("list_size", String.valueOf(list_itemArrayList.size()));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Fragment1", error.toString());
                                // Do something when error occurred
//                                testText.setText("error");
                            }
                        }
                );
                requestQueue.add(jsonArrayRequest);

//                JSONArray contacts = response;
                Log.d("Fragment1", "1");
//                for (int i = 0; i < contacts.length(); i++) {
//                    JSONObject contact = contacts.getJSONObject(i);
//
//                    String name = contact.getString("Name");
//                    String phoneNumber = contact.getString("PhoneNumber");
//                    list.add(new list_item(name, phoneNumber));
//                    Log.d("addwithExistence", "Yeah");
//                }

                adapter = new CustomListAdapter(getActivity(), list);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }

        }
    }

}