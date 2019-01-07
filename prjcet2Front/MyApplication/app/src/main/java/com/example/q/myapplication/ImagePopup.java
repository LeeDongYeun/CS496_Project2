package com.example.q.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class ImagePopup extends Activity{
    private Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_popup);
        mContext = this;

        /** 전송메시지 */
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");

        byte[] imageBytes = Base64.decode(imgPath, Base64.DEFAULT);

        /** 완성된 이미지 보여주기  */
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 8;
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
        iv.setImageBitmap(bm);

    }
}
