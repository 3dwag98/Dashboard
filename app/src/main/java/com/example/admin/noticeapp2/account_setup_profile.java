package com.example.admin.noticeapp2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class account_setup_profile extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 132;
    private ImageView imgProfilepic;
    private EditText txtClass;
    private EditText txtRollno;

    private Uri filePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_profile);

        imgProfilepic = findViewById(R.id.img_profilepic);
        txtClass = findViewById(R.id.txtClass);
        txtRollno = findViewById(R.id.txtRollno);

        imgProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();
            Bitmap btmp = null;

            try {
                btmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                btmp.compress(Bitmap.CompressFormat.JPEG,0,stream);
//                byte[] byteArray = stream.toByteArray();
                Bitmap compressedBitmap = Bitmap.createScaledBitmap(btmp,imgProfilepic.getWidth(),imgProfilepic.getHeight(),true);
                imgProfilepic.setImageBitmap(compressedBitmap);

            } catch (IOException e) {
                imgProfilepic.setBackgroundColor(Color.YELLOW);
            }



        }
    }

    private void chooseFile() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Image"),IMAGE_REQUEST_CODE);
    }

    private void Upload() {
        imgProfilepic.setBackgroundColor(Color.TRANSPARENT);

        // imgUpload.setScaleType(ImageView.ScaleType.FIT_XY);
        chooseFile();
    }
}
