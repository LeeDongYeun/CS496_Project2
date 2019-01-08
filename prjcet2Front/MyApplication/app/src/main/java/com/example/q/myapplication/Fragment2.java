package com.example.q.myapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


@SuppressLint("ValidFragment")
public class Fragment2 extends Fragment {
    String memid;
    String result;
    private Context mContext;
    ArrayList<String> photoList;
    int j = 0;
    ImageAdapter imageAdapter;


    private View view;

    public Fragment2(String memid) {
        this.memid = memid;
        Log.d("aaa", memid);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getContext();
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        photoList = new ArrayList<String>();


        GridView gridView = (GridView) view.findViewById(R.id.ImgGridView);
        imageAdapter = new ImageAdapter(getContext());
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                imageAdapter.callImageViewer(position);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), GallaryAdd.class));
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
                intent.setType("image/*"); //이미지만 보이게
                //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        return view;
    }

    //이미지 선택작업을 후의 결과 처리
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imageString;
        Log.d("aaa", "aaa");
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Log.d("aaa", data.getData().toString());
//                imageString = encodeImage(data.getData().toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] b = baos.toByteArray();
                String encImage = Base64.encodeToString(b, Base64.DEFAULT);
                sedPhotoString(memid, encImage, imageAdapter);
                //System.out.println(encImage);
//                String test = encodeImage(RealPathUtil.getRealPathFromURI_API19(getContext(), data.getData()));
//                Log.d("aaa", test);
//                Log.d("aaa", imageString);

            } else {
                Log.d("aaa", "err");
                //Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private String imgData;
        private String geoData;
        private ArrayList<String> thumbsDataList;
        private ArrayList<String> thumbsIDList;
        //private ArrayList<String> photoList;

        ImageAdapter(Context c) {
            mContext = c;
            thumbsDataList = new ArrayList<String>();
            thumbsIDList = new ArrayList<String>();

            getPhotoArray(memid, photoList, this);
        }

        public final void callImageViewer(int selectedIndex) {
            Intent i = new Intent(mContext, ImagePopup.class);
            String imgPath = photoList.get(selectedIndex);
            i.putExtra("filename", imgPath);
            startActivityForResult(i, 1);
        }

        public boolean deleteSelected(int sIndex) {
            return true;
        }

        public int getCount() {
            return photoList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            RecyclerView recyclerView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                imageView.setMinimumWidth(320);
                imageView.setMinimumHeight(120);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageView = (ImageView) convertView;
            }
            byte[] imageBytes = Base64.decode(photoList.get(position), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bmp);

            return imageView;
        }
    }

    public void getPhotoArray(String memid, final ArrayList<String> photoList, final ImageAdapter adapter) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                                j++;
                                adapter.notifyDataSetChanged();
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

    public void sedPhotoString(String memid, final String fileString, final ImageAdapter adapter) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "http://socrip3.kaist.ac.kr:5580/api/photo/add";
        Log.d("aaa", url);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", memid);
            jsonObject.put("fileString", fileString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    result = response.getString("result");
                    Log.d("aaa", result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("aaa", error.toString());
            }
        });

        jsonRequest.setTag("MAIN");
        requestQueue.add(jsonRequest);
    }


}

