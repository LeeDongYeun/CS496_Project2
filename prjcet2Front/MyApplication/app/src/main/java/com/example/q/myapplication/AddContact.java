package com.example.q.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.q.myapplication.PostPerson;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import Contact.Person;

public class AddContact extends Activity {
    String memid = "d";
    private EditText nameTxt;
    private EditText numberTxt;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        nameTxt = findViewById(R.id.nameTxt);
        numberTxt = findViewById(R.id.numberTxt);
        addBtn = findViewById(R.id.addBtn);
    }

    @Override
    public void onResume(){
        super.onResume();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString();
                String number = numberTxt.getText().toString();
                Person person = new Person();
                person.setName(name);
                person.setNumber(number);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                PostPerson postperson=new PostPerson(person);
                PostPerson postperson = new PostPerson(AddContact.this);

                postperson.postPerson(person, memid);
                Intent intent = new Intent(AddContact.this, FragmentActivity.class);
                startActivity(intent);
            }
        });
    }

}
