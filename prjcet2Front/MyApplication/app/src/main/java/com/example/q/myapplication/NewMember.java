package com.example.q.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewMember extends AppCompatActivity {
    private EditText name;
    private EditText phone;
    RequestQueue queue;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        name = (EditText) findViewById(R.id.message);
        final Button okButton = (Button) findViewById(R.id.okButton);
        final Button canceButton = (Button) findViewById(R.id.cancelButton);
    }

    public void okClick(View view){
        Intent intent = new Intent(this, MainActivity.class);

        EditText login_text = (EditText) findViewById(R.id.member_id);
        EditText password_text = (EditText) findViewById(R.id.password);
        EditText password_text2 = (EditText) findViewById(R.id.check_password);
        EditText name = (EditText) findViewById(R.id.name_db);

        JSONObject jsonObject = new JSONObject();

        if(password_text.getText().toString().equals(password_text2.getText().toString()) &&
                !login_text.getText().toString().equals("") && !name.getText().toString().equals("")){
            try {
                jsonObject.put("id", login_text.getText().toString());
                jsonObject.put("password", password_text.getText().toString());
                jsonObject.put("userName", name.getText().toString());
                Log.d("aaa", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("aaa", "password ok");
            queue = Volley.newRequestQueue(this);
            String url = "http://socrip3.kaist.ac.kr:5580/api/register/";
            Log.d("aaa", url);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        result = response.getString("result");
                        Log.d("aaa", result);
                        toastData(result);

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
        }
        else{
            Log.d("aaa", "password not ok");
            Toast toast = Toast.makeText(this, "모든 항목을 정확히 다 기입했는지 확인해주세요.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void canclick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast toast = Toast.makeText(this, "취소하였습니다.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void toastData(String result){
        Integer resultInt = Integer.parseInt(result);
        Intent intent = new Intent(this, MainActivity.class);

        if(resultInt == 0){
            Toast toast = Toast.makeText(this, "Database Error.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else if(resultInt == 1){
            Toast toast = Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else if(resultInt == 2){
            startActivity(intent);
            Toast toast = Toast.makeText(this, "추가하였습니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
    }


}
