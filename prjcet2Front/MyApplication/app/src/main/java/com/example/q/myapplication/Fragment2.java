package com.example.q.myapplication;
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


public class Fragment2 extends Fragment {
    String memid;
    private Context mContext;
    ArrayList<String> photoList;
    int j = 0;


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

        getPhotoArray(memid, photoList);

        GridView gridView = (GridView) view.findViewById(R.id.ImgGridView);
        final ImageAdapter imageAdapter = new ImageAdapter(getContext());
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                imageAdapter.callImageViewer(position);
            }
        });

        return view;
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
            //photoList = new ArrayList<String>();

            //getPhotoArray(memid, photoList);
            Log.d("aaa","aaa");
            Log.d("aaa", photoList.toString());
            Log.d("aaa","aaa");
            //photoList.add("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");
            //getThumbInfo(thumbsIDList, thumbsDataList);
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
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setAdjustViewBounds(false);
                imageView.setMinimumWidth(320);
                imageView.setMinimumHeight(320);
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

    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;

    }

    public void getPhotoArray(String memid, final ArrayList<String> photoList){

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

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        photoList.add("endOfInput");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("aaa", error.toString());
                        photoList.add("endOfInput");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
        while(photoList.size()!= 0 && photoList.get(photoList.size()-1) != "endOfInput"){

        }
        photoList.remove(photoList.size()-1);
        Log.d("aaa", photoList.toString());
    }

}
