package com.example.q.myapplication;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import Contact.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


//i
public class Fragment1 extends Fragment {
    ListView listView;
    CustomListAdapter adapter;
    ArrayList<list_item> list;
    AlertDialog.Builder builder;
    ArrayList<Person> contact = new ArrayList<>();
    FloatingActionButton floatbutton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        this.listView = (ListView) view.findViewById(R.id.address);
        this.list = new ArrayList<list_item>();
        floatbutton=(FloatingActionButton) view.findViewById(R.id.more);
        adapter = new CustomListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
                                adapter.notifyDataSetChanged();
                                Log.d("aaa", list.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
        return view;}
        @Override public void onResume(){
            super.onResume();

            floatbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Add Contact")        // 제목 설정
                            .setMessage("Are you sure Add Contact?")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(getActivity(), AddContact.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Successfully uploaded.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            });
        }

}