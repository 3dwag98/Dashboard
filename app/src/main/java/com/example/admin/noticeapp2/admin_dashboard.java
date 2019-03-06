package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class admin_dashboard extends AppCompatActivity {

    private ImageButton imgIcon,imgNotice,imgResponse;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        mAuth =  FirebaseAuth.getInstance();

        imgIcon = findViewById(R.id.imgIcon);
        imgNotice = findViewById(R.id.imgAddNotice);
        imgResponse = findViewById(R.id.imgResponse);

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(admin_dashboard.this," Icon Action ",Toast.LENGTH_SHORT).show();
            }
        });

        imgNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),adminnotice_addpage.class));
            }
        });

        imgResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),responceadmin.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(mAuth.getCurrentUser()!=null) {
            mAuth.signOut();
        }
        finish();
        startActivity(new Intent(getApplicationContext(),Login_Window.class));
    }
}
