package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeView extends AppCompatActivity {
    TextView tvTitle, tvDate,tvDesc;
    ImageView  imgdesc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);

        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);

        Intent i = getIntent();
        tvTitle.setText(i.getStringExtra("title"));
        tvDate.setText(i.getStringExtra("time"));
        tvDesc.setText(i.getStringExtra("descrp"));
    }
}
