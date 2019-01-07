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

                Log.d("Fragment1", "Clicked");
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                // Initialize a new JsonArrayRequest instance
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        "http://socrip3.kaist.ac.kr:5580/api/contact/d",
                        new JSONArray(),
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                // Process the JSON
                                try {
                                    JSONArray contacts = response;
                                    Log.d("Fragment1", "1");
                                    for (int i = 0; i < contacts.length(); i++) {
                                        JSONObject contact = contacts.getJSONObject(i);

                                        String name = contact.getString("name");
                                        String phoneNumber = contact.getString("number");
//                                        if (checkExistence != 0) {
                                        list.add(new list_item(name, phoneNumber));
                                        Log.d("addwithExistence", "Yeah");

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