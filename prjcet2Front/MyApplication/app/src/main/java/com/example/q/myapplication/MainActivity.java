package com.example.q.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ImageView imgAvatar;
    EditText login_text, password_text;
    RequestQueue queue;
    String result;
    String userId;
    String password;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        Log.d("aaaa", object.toString());
                        Log.d("aaaa", loginResult.getAccessToken().getUserId());
                        Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                        intent.putExtra("facebook", object.toString());
                        intent.putExtra("key", "login_facebook");
                        startActivity(intent);
                        finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, picture, id, taggable_friends");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void addNewMember(View view){
        Intent intent = new Intent(this, NewMember.class);
        startActivity(intent);
    }

    public void login2(View view) throws JSONException{
        Intent intent = new Intent(this, NewMember.class);
        login_text = (EditText)findViewById(R.id.login_id);
        password_text = (EditText)findViewById(R.id.login_password);

        userId = login_text.getText().toString();
        password = password_text.getText().toString();

        //RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://socrip3.kaist.ac.kr:5580/api/login/" + userId +"/" + password;
        Log.d("aaa", url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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

    public void toastData(String result){
        Integer resultInt = Integer.parseInt(result);
        Intent intent = new Intent(this, NewMember.class);

        if(resultInt == 0){
            Toast toast = Toast.makeText(this, "Database Error.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else if(resultInt == 1){
            Toast toast = Toast.makeText(this, "없는 아이디이거나 틀린 비밀번호입니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else if(resultInt == 2){
            Log.d("aaa", userId);
            intent.putExtra("memberID", userId);
            intent.putExtra("key", "login_own");
            startActivity(intent);
            Toast toast = Toast.makeText(this, "로그인 성공.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else if (resultInt == 3){
            Toast toast = Toast.makeText(this, "없는 아이디이거나 틀린 비밀번호입니다.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "error", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }

}
