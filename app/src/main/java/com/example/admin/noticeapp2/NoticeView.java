package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Arrays;
import java.util.List;

public class NoticeView extends AppCompatActivity {
    TextView tvTitle, tvDate,tvDesc;
    ImageView  imgdesc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);

        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);
        imgdesc = findViewById(R.id.imgdesc);

        List<String> imageFormat = Arrays.asList(new String[]{"jpg", "JPG", "png", "PNG", "jpeg", "JPEG"});


        Intent i = getIntent();
        tvTitle.setText(i.getStringExtra("title"));
        tvDate.setText(i.getStringExtra("time"));
        tvDesc.setText(i.getStringExtra("descrp"));
        String type = i.getStringExtra("type");
        String url = i.getStringExtra("url");

        if(imageFormat.contains(type)&& (!type.equals(""))){
            Glide.with(this)
                    .load(url)
                    .into(imgdesc);
        }

    }
}
